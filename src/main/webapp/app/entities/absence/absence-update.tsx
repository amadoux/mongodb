import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IAbsence } from 'app/shared/model/absence.model';
import { TypeAbsence } from 'app/shared/model/enumerations/type-absence.model';
import { ConfirmationAbsence } from 'app/shared/model/enumerations/confirmation-absence.model';
import { getEntity, updateEntity, createEntity, reset } from './absence.reducer';

export const AbsenceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employees = useAppSelector(state => state.employee.entities);
  const absenceEntity = useAppSelector(state => state.absence.entity);
  const loading = useAppSelector(state => state.absence.loading);
  const updating = useAppSelector(state => state.absence.updating);
  const updateSuccess = useAppSelector(state => state.absence.updateSuccess);
  const typeAbsenceValues = Object.keys(TypeAbsence);
  const confirmationAbsenceValues = Object.keys(ConfirmationAbsence);

  const handleClose = () => {
    navigate('/absence' + location.search);
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
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    if (values.numberDayAbsence !== undefined && typeof values.numberDayAbsence !== 'number') {
      values.numberDayAbsence = Number(values.numberDayAbsence);
    }
    if (values.congeRestant !== undefined && typeof values.congeRestant !== 'number') {
      values.congeRestant = Number(values.congeRestant);
    }

    const entity = {
      ...absenceEntity,
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
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          typeAbsence: 'RTT',
          confirmationAbsence: 'ENCOURS',
          ...absenceEntity,
          startDate: convertDateTimeFromServer(absenceEntity.startDate),
          endDate: convertDateTimeFromServer(absenceEntity.endDate),
          employee: absenceEntity?.employee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mondodbApp.absence.home.createOrEditLabel" data-cy="AbsenceCreateUpdateHeading">
            <Translate contentKey="mondodbApp.absence.home.createOrEditLabel">Create or edit a Absence</Translate>
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
                  id="absence-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mondodbApp.absence.startDate')}
                id="absence-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.absence.endDate')}
                id="absence-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.absence.numberDayAbsence')}
                id="absence-numberDayAbsence"
                name="numberDayAbsence"
                data-cy="numberDayAbsence"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.absence.typeAbsence')}
                id="absence-typeAbsence"
                name="typeAbsence"
                data-cy="typeAbsence"
                type="select"
              >
                {typeAbsenceValues.map(typeAbsence => (
                  <option value={typeAbsence} key={typeAbsence}>
                    {translate('mondodbApp.TypeAbsence.' + typeAbsence)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.absence.confirmationAbsence')}
                id="absence-confirmationAbsence"
                name="confirmationAbsence"
                data-cy="confirmationAbsence"
                type="select"
              >
                {confirmationAbsenceValues.map(confirmationAbsence => (
                  <option value={confirmationAbsence} key={confirmationAbsence}>
                    {translate('mondodbApp.ConfirmationAbsence.' + confirmationAbsence)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.absence.congeRestant')}
                id="absence-congeRestant"
                name="congeRestant"
                data-cy="congeRestant"
                type="text"
              />
              <ValidatedField
                id="absence-employee"
                name="employee"
                data-cy="employee"
                label={translate('mondodbApp.absence.employee')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/absence" replace color="info">
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

export default AbsenceUpdate;
