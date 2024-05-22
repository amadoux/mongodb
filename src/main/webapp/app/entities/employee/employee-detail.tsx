import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employee.reducer';

export const EmployeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeeEntity = useAppSelector(state => state.employee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeeDetailsHeading">
          <Translate contentKey="mondodbApp.employee.detail.title">Employee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="mondodbApp.employee.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="mondodbApp.employee.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="mondodbApp.employee.email">Email</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="mondodbApp.employee.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.phoneNumber}</dd>
          <dt>
            <span id="identityCard">
              <Translate contentKey="mondodbApp.employee.identityCard">Identity Card</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.identityCard}</dd>
          <dt>
            <span id="dateInspiration">
              <Translate contentKey="mondodbApp.employee.dateInspiration">Date Inspiration</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.dateInspiration ? (
              <TextFormat value={employeeEntity.dateInspiration} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="nationality">
              <Translate contentKey="mondodbApp.employee.nationality">Nationality</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.nationality}</dd>
          <dt>
            <span id="uploadIdentityCard">
              <Translate contentKey="mondodbApp.employee.uploadIdentityCard">Upload Identity Card</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.uploadIdentityCard ? (
              <div>
                {employeeEntity.uploadIdentityCardContentType ? (
                  <a onClick={openFile(employeeEntity.uploadIdentityCardContentType, employeeEntity.uploadIdentityCard)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {employeeEntity.uploadIdentityCardContentType}, {byteSize(employeeEntity.uploadIdentityCard)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="typeEmployed">
              <Translate contentKey="mondodbApp.employee.typeEmployed">Type Employed</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.typeEmployed}</dd>
          <dt>
            <span id="cityAgency">
              <Translate contentKey="mondodbApp.employee.cityAgency">City Agency</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.cityAgency}</dd>
          <dt>
            <span id="residenceCity">
              <Translate contentKey="mondodbApp.employee.residenceCity">Residence City</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.residenceCity}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="mondodbApp.employee.address">Address</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.address}</dd>
          <dt>
            <span id="socialSecurityNumber">
              <Translate contentKey="mondodbApp.employee.socialSecurityNumber">Social Security Number</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.socialSecurityNumber}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="mondodbApp.employee.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.birthDate ? <TextFormat value={employeeEntity.birthDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="birthPlace">
              <Translate contentKey="mondodbApp.employee.birthPlace">Birth Place</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.birthPlace}</dd>
          <dt>
            <span id="entryDate">
              <Translate contentKey="mondodbApp.employee.entryDate">Entry Date</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.entryDate ? <TextFormat value={employeeEntity.entryDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="releaseDate">
              <Translate contentKey="mondodbApp.employee.releaseDate">Release Date</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.releaseDate ? <TextFormat value={employeeEntity.releaseDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="workstation">
              <Translate contentKey="mondodbApp.employee.workstation">Workstation</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.workstation}</dd>
          <dt>
            <span id="descriptionWorkstation">
              <Translate contentKey="mondodbApp.employee.descriptionWorkstation">Description Workstation</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.descriptionWorkstation}</dd>
          <dt>
            <span id="department">
              <Translate contentKey="mondodbApp.employee.department">Department</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.department}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="mondodbApp.employee.level">Level</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.level}</dd>
          <dt>
            <span id="coefficient">
              <Translate contentKey="mondodbApp.employee.coefficient">Coefficient</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.coefficient}</dd>
          <dt>
            <span id="numberHours">
              <Translate contentKey="mondodbApp.employee.numberHours">Number Hours</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.numberHours}</dd>
          <dt>
            <span id="averageHourlyCost">
              <Translate contentKey="mondodbApp.employee.averageHourlyCost">Average Hourly Cost</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.averageHourlyCost}</dd>
          <dt>
            <span id="monthlyGrossAmount">
              <Translate contentKey="mondodbApp.employee.monthlyGrossAmount">Monthly Gross Amount</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.monthlyGrossAmount}</dd>
          <dt>
            <span id="commissionAmount">
              <Translate contentKey="mondodbApp.employee.commissionAmount">Commission Amount</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.commissionAmount}</dd>
          <dt>
            <span id="contractType">
              <Translate contentKey="mondodbApp.employee.contractType">Contract Type</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.contractType}</dd>
          <dt>
            <span id="salaryType">
              <Translate contentKey="mondodbApp.employee.salaryType">Salary Type</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.salaryType}</dd>
          <dt>
            <span id="hireDate">
              <Translate contentKey="mondodbApp.employee.hireDate">Hire Date</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.hireDate ? <TextFormat value={employeeEntity.hireDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="mondodbApp.employee.enterprise">Enterprise</Translate>
          </dt>
          <dd>{employeeEntity.enterprise ? employeeEntity.enterprise.companyName : ''}</dd>
          <dt>
            <Translate contentKey="mondodbApp.employee.managerEmployee">Manager Employee</Translate>
          </dt>
          <dd>{employeeEntity.managerEmployee ? employeeEntity.managerEmployee.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee/${employeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeeDetail;
