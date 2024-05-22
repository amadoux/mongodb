import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './contract.reducer';

export const Contract = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const contractList = useAppSelector(state => state.contract.entities);
  const loading = useAppSelector(state => state.contract.loading);
  const totalItems = useAppSelector(state => state.contract.totalItems);

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
      <h2 id="contract-heading" data-cy="ContractHeading">
        <Translate contentKey="mondodbApp.contract.home.title">Contracts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mondodbApp.contract.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/contract/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mondodbApp.contract.home.createLabel">Create new Contract</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {contractList && contractList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mondodbApp.contract.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('contractType')}>
                  <Translate contentKey="mondodbApp.contract.contractType">Contract Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contractType')} />
                </th>
                <th className="hand" onClick={sort('entryDate')}>
                  <Translate contentKey="mondodbApp.contract.entryDate">Entry Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entryDate')} />
                </th>
                <th className="hand" onClick={sort('releaseDate')}>
                  <Translate contentKey="mondodbApp.contract.releaseDate">Release Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('releaseDate')} />
                </th>
                <th className="hand" onClick={sort('statusContract')}>
                  <Translate contentKey="mondodbApp.contract.statusContract">Status Contract</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('statusContract')} />
                </th>
                <th className="hand" onClick={sort('uploadContract')}>
                  <Translate contentKey="mondodbApp.contract.uploadContract">Upload Contract</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uploadContract')} />
                </th>
                <th>
                  <Translate contentKey="mondodbApp.contract.employee">Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contractList.map((contract, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/contract/${contract.id}`} color="link" size="sm">
                      {contract.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`mondodbApp.ContractType.${contract.contractType}`} />
                  </td>
                  <td>{contract.entryDate ? <TextFormat type="date" value={contract.entryDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{contract.releaseDate ? <TextFormat type="date" value={contract.releaseDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`mondodbApp.StatusContract.${contract.statusContract}`} />
                  </td>
                  <td>
                    {contract.uploadContract ? (
                      <div>
                        {contract.uploadContractContentType ? (
                          <a onClick={openFile(contract.uploadContractContentType, contract.uploadContract)}>
                            <img
                              src={`data:${contract.uploadContractContentType};base64,${contract.uploadContract}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {contract.uploadContractContentType}, {byteSize(contract.uploadContract)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{contract.employee ? <Link to={`/employee/${contract.employee.id}`}>{contract.employee.email}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/contract/${contract.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/contract/${contract.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/contract/${contract.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="mondodbApp.contract.home.notFound">No Contracts found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={contractList && contractList.length > 0 ? '' : 'd-none'}>
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

export default Contract;
