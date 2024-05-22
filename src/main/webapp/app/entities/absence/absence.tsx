import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './absence.reducer';

export const Absence = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const absenceList = useAppSelector(state => state.absence.entities);
  const loading = useAppSelector(state => state.absence.loading);
  const totalItems = useAppSelector(state => state.absence.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="absence-heading" data-cy="AbsenceHeading">
        <Translate contentKey="mondodbApp.absence.home.title">Absences</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mondodbApp.absence.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/absence/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mondodbApp.absence.home.createLabel">Create new Absence</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {absenceList && absenceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mondodbApp.absence.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="mondodbApp.absence.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="mondodbApp.absence.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('numberDayAbsence')}>
                  <Translate contentKey="mondodbApp.absence.numberDayAbsence">Number Day Absence</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberDayAbsence')} />
                </th>
                <th className="hand" onClick={sort('typeAbsence')}>
                  <Translate contentKey="mondodbApp.absence.typeAbsence">Type Absence</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('typeAbsence')} />
                </th>
                <th className="hand" onClick={sort('confirmationAbsence')}>
                  <Translate contentKey="mondodbApp.absence.confirmationAbsence">Confirmation Absence</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('confirmationAbsence')} />
                </th>
                <th className="hand" onClick={sort('congeRestant')}>
                  <Translate contentKey="mondodbApp.absence.congeRestant">Conge Restant</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('congeRestant')} />
                </th>
                <th>
                  <Translate contentKey="mondodbApp.absence.employee">Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {absenceList.map((absence, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/absence/${absence.id}`} color="link" size="sm">
                      {absence.id}
                    </Button>
                  </td>
                  <td>{absence.startDate ? <TextFormat type="date" value={absence.startDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{absence.endDate ? <TextFormat type="date" value={absence.endDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{absence.numberDayAbsence}</td>
                  <td>
                    <Translate contentKey={`mondodbApp.TypeAbsence.${absence.typeAbsence}`} />
                  </td>
                  <td>
                    <Translate contentKey={`mondodbApp.ConfirmationAbsence.${absence.confirmationAbsence}`} />
                  </td>
                  <td>{absence.congeRestant}</td>
                  <td>{absence.employee ? <Link to={`/employee/${absence.employee.id}`}>{absence.employee.email}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/absence/${absence.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/absence/${absence.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/absence/${absence.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="mondodbApp.absence.home.notFound">No Absences found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={absenceList && absenceList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Absence;
