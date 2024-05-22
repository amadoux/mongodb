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
import { IEnterprise } from 'app/shared/model/enterprise.model';
import { getEntities as getEnterprises } from 'app/entities/enterprise/enterprise.reducer';
import { ISocialCharges } from 'app/shared/model/social-charges.model';
import { SPentType } from 'app/shared/model/enumerations/s-pent-type.model';
import { StatusCharges } from 'app/shared/model/enumerations/status-charges.model';
import { getEntity, updateEntity, createEntity, reset } from './social-charges.reducer';

export const SocialChargesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employees = useAppSelector(state => state.employee.entities);
  const enterprises = useAppSelector(state => state.enterprise.entities);
  const socialChargesEntity = useAppSelector(state => state.socialCharges.entity);
  const loading = useAppSelector(state => state.socialCharges.loading);
  const updating = useAppSelector(state => state.socialCharges.updating);
  const updateSuccess = useAppSelector(state => state.socialCharges.updateSuccess);
  const sPentTypeValues = Object.keys(SPentType);
  const statusChargesValues = Object.keys(StatusCharges);

  const handleClose = () => {
    navigate('/social-charges' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEmployees({}));
    dispatch(getEnterprises({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    values.spentDate = convertDateTimeToServer(values.spentDate);
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }

    const entity = {
      ...socialChargesEntity,
      ...values,
      responsableDepense: employees.find(it => it.id.toString() === values.responsableDepense?.toString()),
      enterprise: enterprises.find(it => it.id.toString() === values.enterprise?.toString()),
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
          spentDate: displayDefaultDateTime(),
        }
      : {
          spentType: 'MATERIAL',
          statusCharges: 'IN_PROGRESS',
          ...socialChargesEntity,
          spentDate: convertDateTimeFromServer(socialChargesEntity.spentDate),
          responsableDepense: socialChargesEntity?.responsableDepense?.id,
          enterprise: socialChargesEntity?.enterprise?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mondodbApp.socialCharges.home.createOrEditLabel" data-cy="SocialChargesCreateUpdateHeading">
            <Translate contentKey="mondodbApp.socialCharges.home.createOrEditLabel">Create or edit a SocialCharges</Translate>
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
                  id="social-charges-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mondodbApp.socialCharges.spentDate')}
                id="social-charges-spentDate"
                name="spentDate"
                data-cy="spentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('mondodbApp.socialCharges.spentType')}
                id="social-charges-spentType"
                name="spentType"
                data-cy="spentType"
                type="select"
              >
                {sPentTypeValues.map(sPentType => (
                  <option value={sPentType} key={sPentType}>
                    {translate('mondodbApp.SPentType.' + sPentType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.socialCharges.statusCharges')}
                id="social-charges-statusCharges"
                name="statusCharges"
                data-cy="statusCharges"
                type="select"
              >
                {statusChargesValues.map(statusCharges => (
                  <option value={statusCharges} key={statusCharges}>
                    {translate('mondodbApp.StatusCharges.' + statusCharges)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mondodbApp.socialCharges.amount')}
                id="social-charges-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.socialCharges.commentText')}
                id="social-charges-commentText"
                name="commentText"
                data-cy="commentText"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="social-charges-responsableDepense"
                name="responsableDepense"
                data-cy="responsableDepense"
                label={translate('mondodbApp.socialCharges.responsableDepense')}
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
              <ValidatedField
                id="social-charges-enterprise"
                name="enterprise"
                data-cy="enterprise"
                label={translate('mondodbApp.socialCharges.enterprise')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/social-charges" replace color="info">
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

export default SocialChargesUpdate;
