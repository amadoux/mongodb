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

import { getEntities } from './pay-slip.reducer';

export const PaySlip = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const paySlipList = useAppSelector(state => state.paySlip.entities);
  const loading = useAppSelector(state => state.paySlip.loading);
  const totalItems = useAppSelector(state => state.paySlip.totalItems);

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
      <h2 id="pay-slip-heading" data-cy="PaySlipHeading">
        <Translate contentKey="mondodbApp.paySlip.home.title">Pay Slips</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mondodbApp.paySlip.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pay-slip/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mondodbApp.paySlip.home.createLabel">Create new Pay Slip</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {paySlipList && paySlipList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mondodbApp.paySlip.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('netSalaryPay')}>
                  <Translate contentKey="mondodbApp.paySlip.netSalaryPay">Net Salary Pay</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('netSalaryPay')} />
                </th>
                <th className="hand" onClick={sort('paySlipDate')}>
                  <Translate contentKey="mondodbApp.paySlip.paySlipDate">Pay Slip Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paySlipDate')} />
                </th>
                <th className="hand" onClick={sort('uploadPaySlip')}>
                  <Translate contentKey="mondodbApp.paySlip.uploadPaySlip">Upload Pay Slip</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uploadPaySlip')} />
                </th>
                <th>
                  <Translate contentKey="mondodbApp.paySlip.employee">Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paySlipList.map((paySlip, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pay-slip/${paySlip.id}`} color="link" size="sm">
                      {paySlip.id}
                    </Button>
                  </td>
                  <td>{paySlip.netSalaryPay}</td>
                  <td>{paySlip.paySlipDate ? <TextFormat type="date" value={paySlip.paySlipDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {paySlip.uploadPaySlip ? (
                      <div>
                        {paySlip.uploadPaySlipContentType ? (
                          <a onClick={openFile(paySlip.uploadPaySlipContentType, paySlip.uploadPaySlip)}>
                            <img
                              src={`data:${paySlip.uploadPaySlipContentType};base64,${paySlip.uploadPaySlip}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {paySlip.uploadPaySlipContentType}, {byteSize(paySlip.uploadPaySlip)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{paySlip.employee ? <Link to={`/employee/${paySlip.employee.id}`}>{paySlip.employee.email}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pay-slip/${paySlip.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/pay-slip/${paySlip.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/pay-slip/${paySlip.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="mondodbApp.paySlip.home.notFound">No Pay Slips found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={paySlipList && paySlipList.length > 0 ? '' : 'd-none'}>
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

export default PaySlip;
