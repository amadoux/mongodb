import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IContract } from 'app/shared/model/contract.model';
import { ContractType } from 'app/shared/model/enumerations/contract-type.model';
import { StatusContract } from 'app/shared/model/enumerations/status-contract.model';
import { getEntity, updateEntity, createEntity, reset } from './contract.reducer';

export const ContractUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employees = useAppSelector(state => state.employee.entities);
  const contractEntity = useAppSelector(state => state.contract.entity);
  const loading = useAppSelector(state => state.contract.loading);
  const updating = useAppSelector(state => state.contract.updating);
  const updateSuccess = useAppSelector(state => state.contract.updateSuccess);
  const contractTypeValues = Object.keys(ContractType);
  const statusContractValues = Object.keys(StatusContract);

  const handleClose = () => {
    navigate('/contract' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEmployees({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    values.entryDate = convertDateTimeToServer(values.entryDate);
    values.releaseDate = convertDateTimeToServer(values.releaseDate);

    const entity = {
      ...contractEntity,
      ...values,
      employee: employees.find(it => it.id.toString() === values.employee?.toString()),
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
          entryDate: displayDefaultDateTime(),
          releaseDate: displayDefaultDateTime(),
        }
      : {
          contractType: 'CDD',
          statusContract: 'ENCOURS',
          ...contractEntity,
          entryDate: convertDateTimeFromServer(contractEntity.entryDate),
          releaseDate: convertDateTimeFromServer(contractEntity.releaseDate),
          employee: contractEntity?.employee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mondodbApp.contract.home.createOrEditLabel" data-cy="ContractCreateUpdateHeading">
            <Translate contentKey="mondodbApp.contract.home.createOrEditLabel">Create or edit a Contract</Translate>
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
                  id="contract-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mondodbApp.contract.contractType')}
                id="contract-contractType"
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
              <UncontrolledTooltip target="contractTypeLabel">
                <Translate contentKey="mondodbApp.contract.help.contractType" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('mondodbApp.contract.entryDate')}
                id="contract-entryDate"
                name="entryDate"
                data-cy="entryDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.contract.releaseDate')}
                id="contract-releaseDate"
                name="releaseDate"
                data-cy="releaseDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.contract.statusContract')}
                id="contract-statusContract"
                name="statusContract"
                data-cy="statusContract"
                type="select"
              >
                {statusContractValues.map(statusContract => (
                  <option value={statusContract} key={statusContract}>
                    {translate('mondodbApp.StatusContract.' + statusContract)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField
                label={translate('mondodbApp.contract.uploadContract')}
                id="contract-uploadContract"
                name="uploadContract"
                data-cy="uploadContract"
                isImage
                accept="image/*"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="contract-employee"
                name="employee"
                data-cy="employee"
                label={translate('mondodbApp.contract.employee')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/contract" replace color="info">
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

export default ContractUpdate;
