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

import { getEntities } from './employee.reducer';

export const Employee = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const employeeList = useAppSelector(state => state.employee.entities);
  const loading = useAppSelector(state => state.employee.loading);
  const totalItems = useAppSelector(state => state.employee.totalItems);

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
      <h2 id="employee-heading" data-cy="EmployeeHeading">
        <Translate contentKey="mondodbApp.employee.home.title">Employees</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mondodbApp.employee.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/employee/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mondodbApp.employee.home.createLabel">Create new Employee</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {employeeList && employeeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mondodbApp.employee.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="mondodbApp.employee.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="mondodbApp.employee.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="mondodbApp.employee.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="mondodbApp.employee.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('identityCard')}>
                  <Translate contentKey="mondodbApp.employee.identityCard">Identity Card</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('identityCard')} />
                </th>
                <th className="hand" onClick={sort('dateInspiration')}>
                  <Translate contentKey="mondodbApp.employee.dateInspiration">Date Inspiration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateInspiration')} />
                </th>
                <th className="hand" onClick={sort('nationality')}>
                  <Translate contentKey="mondodbApp.employee.nationality">Nationality</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nationality')} />
                </th>
                <th className="hand" onClick={sort('uploadIdentityCard')}>
                  <Translate contentKey="mondodbApp.employee.uploadIdentityCard">Upload Identity Card</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uploadIdentityCard')} />
                </th>
                <th className="hand" onClick={sort('typeEmployed')}>
                  <Translate contentKey="mondodbApp.employee.typeEmployed">Type Employed</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('typeEmployed')} />
                </th>
                <th className="hand" onClick={sort('cityAgency')}>
                  <Translate contentKey="mondodbApp.employee.cityAgency">City Agency</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cityAgency')} />
                </th>
                <th className="hand" onClick={sort('residenceCity')}>
                  <Translate contentKey="mondodbApp.employee.residenceCity">Residence City</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('residenceCity')} />
                </th>
                <th className="hand" onClick={sort('address')}>
                  <Translate contentKey="mondodbApp.employee.address">Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('address')} />
                </th>
                <th className="hand" onClick={sort('socialSecurityNumber')}>
                  <Translate contentKey="mondodbApp.employee.socialSecurityNumber">Social Security Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('socialSecurityNumber')} />
                </th>
                <th className="hand" onClick={sort('birthDate')}>
                  <Translate contentKey="mondodbApp.employee.birthDate">Birth Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthDate')} />
                </th>
                <th className="hand" onClick={sort('birthPlace')}>
                  <Translate contentKey="mondodbApp.employee.birthPlace">Birth Place</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthPlace')} />
                </th>
                <th className="hand" onClick={sort('entryDate')}>
                  <Translate contentKey="mondodbApp.employee.entryDate">Entry Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entryDate')} />
                </th>
                <th className="hand" onClick={sort('releaseDate')}>
                  <Translate contentKey="mondodbApp.employee.releaseDate">Release Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('releaseDate')} />
                </th>
                <th className="hand" onClick={sort('workstation')}>
                  <Translate contentKey="mondodbApp.employee.workstation">Workstation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('workstation')} />
                </th>
                <th className="hand" onClick={sort('descriptionWorkstation')}>
                  <Translate contentKey="mondodbApp.employee.descriptionWorkstation">Description Workstation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descriptionWorkstation')} />
                </th>
                <th className="hand" onClick={sort('department')}>
                  <Translate contentKey="mondodbApp.employee.department">Department</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('department')} />
                </th>
                <th className="hand" onClick={sort('level')}>
                  <Translate contentKey="mondodbApp.employee.level">Level</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('level')} />
                </th>
                <th className="hand" onClick={sort('coefficient')}>
                  <Translate contentKey="mondodbApp.employee.coefficient">Coefficient</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('coefficient')} />
                </th>
                <th className="hand" onClick={sort('numberHours')}>
                  <Translate contentKey="mondodbApp.employee.numberHours">Number Hours</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberHours')} />
                </th>
                <th className="hand" onClick={sort('averageHourlyCost')}>
                  <Translate contentKey="mondodbApp.employee.averageHourlyCost">Average Hourly Cost</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('averageHourlyCost')} />
                </th>
                <th className="hand" onClick={sort('monthlyGrossAmount')}>
                  <Translate contentKey="mondodbApp.employee.monthlyGrossAmount">Monthly Gross Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('monthlyGrossAmount')} />
                </th>
                <th className="hand" onClick={sort('commissionAmount')}>
                  <Translate contentKey="mondodbApp.employee.commissionAmount">Commission Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('commissionAmount')} />
                </th>
                <th className="hand" onClick={sort('contractType')}>
                  <Translate contentKey="mondodbApp.employee.contractType">Contract Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contractType')} />
                </th>
                <th className="hand" onClick={sort('salaryType')}>
                  <Translate contentKey="mondodbApp.employee.salaryType">Salary Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('salaryType')} />
                </th>
                <th className="hand" onClick={sort('hireDate')}>
                  <Translate contentKey="mondodbApp.employee.hireDate">Hire Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('hireDate')} />
                </th>
                <th>
                  <Translate contentKey="mondodbApp.employee.enterprise">Enterprise</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mondodbApp.employee.managerEmployee">Manager Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {employeeList.map((employee, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/employee/${employee.id}`} color="link" size="sm">
                      {employee.id}
                    </Button>
                  </td>
                  <td>{employee.firstName}</td>
                  <td>{employee.lastName}</td>
                  <td>{employee.email}</td>
                  <td>{employee.phoneNumber}</td>
                  <td>{employee.identityCard}</td>
                  <td>
                    {employee.dateInspiration ? <TextFormat type="date" value={employee.dateInspiration} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`mondodbApp.Pays.${employee.nationality}`} />
                  </td>
                  <td>
                    {employee.uploadIdentityCard ? (
                      <div>
                        {employee.uploadIdentityCardContentType ? (
                          <a onClick={openFile(employee.uploadIdentityCardContentType, employee.uploadIdentityCard)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {employee.uploadIdentityCardContentType}, {byteSize(employee.uploadIdentityCard)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`mondodbApp.TypeEmployed.${employee.typeEmployed}`} />
                  </td>
                  <td>{employee.cityAgency}</td>
                  <td>{employee.residenceCity}</td>
                  <td>{employee.address}</td>
                  <td>{employee.socialSecurityNumber}</td>
                  <td>{employee.birthDate ? <TextFormat type="date" value={employee.birthDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{employee.birthPlace}</td>
                  <td>{employee.entryDate ? <TextFormat type="date" value={employee.entryDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{employee.releaseDate ? <TextFormat type="date" value={employee.releaseDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{employee.workstation}</td>
                  <td>{employee.descriptionWorkstation}</td>
                  <td>
                    <Translate contentKey={`mondodbApp.Department.${employee.department}`} />
                  </td>
                  <td>
                    <Translate contentKey={`mondodbApp.Level.${employee.level}`} />
                  </td>
                  <td>{employee.coefficient}</td>
                  <td>{employee.numberHours}</td>
                  <td>{employee.averageHourlyCost}</td>
                  <td>{employee.monthlyGrossAmount}</td>
                  <td>{employee.commissionAmount}</td>
                  <td>
                    <Translate contentKey={`mondodbApp.ContractType.${employee.contractType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`mondodbApp.SalaryType.${employee.salaryType}`} />
                  </td>
                  <td>{employee.hireDate ? <TextFormat type="date" value={employee.hireDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {employee.enterprise ? <Link to={`/enterprise/${employee.enterprise.id}`}>{employee.enterprise.companyName}</Link> : ''}
                  </td>
                  <td>
                    {employee.managerEmployee ? (
                      <Link to={`/employee/${employee.managerEmployee.id}`}>{employee.managerEmployee.email}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/employee/${employee.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/employee/${employee.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/employee/${employee.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="mondodbApp.employee.home.notFound">No Employees found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={employeeList && employeeList.length > 0 ? '' : 'd-none'}>
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

export default Employee;
