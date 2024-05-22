import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEnterprise } from 'app/shared/model/enterprise.model';
import { getEntities as getEnterprises } from 'app/entities/enterprise/enterprise.reducer';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { Pays } from 'app/shared/model/enumerations/pays.model';
import { TypeEmployed } from 'app/shared/model/enumerations/type-employed.model';
import { Department } from 'app/shared/model/enumerations/department.model';
import { Level } from 'app/shared/model/enumerations/level.model';
import { ContractType } from 'app/shared/model/enumerations/contract-type.model';
import { SalaryType } from 'app/shared/model/enumerations/salary-type.model';
import { getEntity, updateEntity, createEntity, reset } from './employee.reducer';

export const EmployeeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const enterprises = useAppSelector(state => state.enterprise.entities);
  const employees = useAppSelector(state => state.employee.entities);
  const employeeEntity = useAppSelector(state => state.employee.entity);
  const loading = useAppSelector(state => state.employee.loading);
  const updating = useAppSelector(state => state.employee.updating);
  const updateSuccess = useAppSelector(state => state.employee.updateSuccess);
  const paysValues = Object.keys(Pays);
  const typeEmployedValues = Object.keys(TypeEmployed);
  const departmentValues = Object.keys(Department);
  const levelValues = Object.keys(Level);
  const contractTypeValues = Object.keys(ContractType);
  const salaryTypeValues = Object.keys(SalaryType);

  const handleClose = () => {
    navigate('/employee' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEnterprises({}));
    dispatch(getEmployees({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    values.dateInspiration = convertDateTimeToServer(values.dateInspiration);
    values.birthDate = convertDateTimeToServer(values.birthDate);
    values.entryDate = convertDateTimeToServer(values.entryDate);
    values.releaseDate = convertDateTimeToServer(values.releaseDate);
    if (values.coefficient !== undefined && typeof values.coefficient !== 'number') {
      values.coefficient = Number(values.coefficient);
    }
    if (values.monthlyGrossAmount !== undefined && typeof values.monthlyGrossAmount !== 'number') {
      values.monthlyGrossAmount = Number(values.monthlyGrossAmount);
    }
    if (values.commissionAmount !== undefined && typeof values.commissionAmount !== 'number') {
      values.commissionAmount = Number(values.commissionAmount);
    }
    values.hireDate = convertDateTimeToServer(values.hireDate);

    const entity = {
      ...employeeEntity,
      ...values,
      enterprise: enterprises.find(it => it.id.toString() === values.enterprise?.toString()),
      managerEmployee: employees.find(it => it.id.toString() === values.managerEmployee?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateInspiration: displayDefaultDateTime(),
          birthDate: displayDefaultDateTime(),
          entryDate: displayDefaultDateTime(),
          releaseDate: displayDefaultDateTime(),
          hireDate: displayDefaultDateTime(),
        }
      : {
          nationality: 'CAMEROON',
          typeEmployed: 'MARKETER',
          department: 'Production',
          level: 'A',
          contractType: 'CDD',
          salaryType: 'EXECUTIVE_SALARIED',
          ...employeeEntity,
          dateInspiration: convertDateTimeFromServer(employeeEntity.dateInspiration),
          birthDate: convertDateTimeFromServer(employeeEntity.birthDate),
          entryDate: convertDateTimeFromServer(employeeEntity.entryDate),
          releaseDate: convertDateTimeFromServer(employeeEntity.releaseDate),
          hireDate: convertDateTimeFromServer(employeeEntity.hireDate),
          enterprise: employeeEntity?.enterprise?.id,
          managerEmployee: employeeEntity?.managerEmployee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mondodbApp.employee.home.createOrEditLabel" data-cy="EmployeeCreateUpdateHeading">
            <Translate contentKey="mondodbApp.employee.home.createOrEditLabel">Create or edit a Employee</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="employee-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mondodbApp.employee.firstName')}
                id="employee-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.lastName')}
                id="employee-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.email')}
                id="employee-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.employee.phoneNumber')}
                id="employee-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.employee.identityCard')}
                id="employee-identityCard"
                name="identityCard"
                data-cy="identityCard"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.employee.dateInspiration')}
                id="employee-dateInspiration"
                name="dateInspiration"
                data-cy="dateInspiration"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.nationality')}
                id="employee-nationality"
                name="nationality"
                data-cy="nationality"
                type="select"
              >
                {paysValues.map(pays => (
                  <option value={pays} key={pays}>
                    {translate('mondodbApp.Pays.' + pays)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField
                label={translate('mondodbApp.employee.uploadIdentityCard')}
                id="employee-uploadIdentityCard"
                name="uploadIdentityCard"
                data-cy="uploadIdentityCard"
                openActionLabel={translate('entity.action.open')}
                validate={{}}
              />
              <ValidatedField
                label={translate('mondodbApp.employee.typeEmployed')}
                id="employee-typeEmployed"
                name="typeEmployed"
                data-cy="typeEmployed"
                type="select"
              >
                {typeEmployedValues.map(typeEmployed => (
                  <option value={typeEmployed} key={typeEmployed}>
                    {translate('mondodbApp.TypeEmployed.' + typeEmployed)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.employee.cityAgency')}
                id="employee-cityAgency"
                name="cityAgency"
                data-cy="cityAgency"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.residenceCity')}
                id="employee-residenceCity"
                name="residenceCity"
                data-cy="residenceCity"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.address')}
                id="employee-address"
                name="address"
                data-cy="address"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.socialSecurityNumber')}
                id="employee-socialSecurityNumber"
                name="socialSecurityNumber"
                data-cy="socialSecurityNumber"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.birthDate')}
                id="employee-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.birthPlace')}
                id="employee-birthPlace"
                name="birthPlace"
                data-cy="birthPlace"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.entryDate')}
                id="employee-entryDate"
                name="entryDate"
                data-cy="entryDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.releaseDate')}
                id="employee-releaseDate"
                name="releaseDate"
                data-cy="releaseDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.workstation')}
                id="employee-workstation"
                name="workstation"
                data-cy="workstation"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.descriptionWorkstation')}
                id="employee-descriptionWorkstation"
                name="descriptionWorkstation"
                data-cy="descriptionWorkstation"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.department')}
                id="employee-department"
                name="department"
                data-cy="department"
                type="select"
              >
                {departmentValues.map(department => (
                  <option value={department} key={department}>
                    {translate('mondodbApp.Department.' + department)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('mondodbApp.employee.level')} id="employee-level" name="level" data-cy="level" type="select">
                {levelValues.map(level => (
                  <option value={level} key={level}>
                    {translate('mondodbApp.Level.' + level)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.employee.coefficient')}
                id="employee-coefficient"
                name="coefficient"
                data-cy="coefficient"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.numberHours')}
                id="employee-numberHours"
                name="numberHours"
                data-cy="numberHours"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.averageHourlyCost')}
                id="employee-averageHourlyCost"
                name="averageHourlyCost"
                data-cy="averageHourlyCost"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.monthlyGrossAmount')}
                id="employee-monthlyGrossAmount"
                name="monthlyGrossAmount"
                data-cy="monthlyGrossAmount"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.commissionAmount')}
                id="employee-commissionAmount"
                name="commissionAmount"
                data-cy="commissionAmount"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.employee.contractType')}
                id="employee-contractType"
                name="contractType"
                data-cy="contractType"
                type="select"
              >
                {contractTypeValues.map(contractType => (
                  <option value={contractType} key={contractType}>
                    {translate('mondodbApp.ContractType.' + contractType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.employee.salaryType')}
                id="employee-salaryType"
                name="salaryType"
                data-cy="salaryType"
                type="select"
              >
                {salaryTypeValues.map(salaryType => (
                  <option value={salaryType} key={salaryType}>
                    {translate('mondodbApp.SalaryType.' + salaryType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.employee.hireDate')}
                id="employee-hireDate"
                name="hireDate"
                data-cy="hireDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="employee-enterprise"
                name="enterprise"
                data-cy="enterprise"
                label={translate('mondodbApp.employee.enterprise')}
                type="select"
              >
                <option value="" key="0" />
                {enterprises
                  ? enterprises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.companyName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="employee-managerEmployee"
                name="managerEmployee"
                data-cy="managerEmployee"
                label={translate('mondodbApp.employee.managerEmployee')}
                type="select"
              >
                <option value="" key="0" />
                {employees
                  ? employees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.email}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/employee" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EmployeeUpdate;
