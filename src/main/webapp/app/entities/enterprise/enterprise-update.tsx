import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEnterprise } from 'app/shared/model/enterprise.model';
import { Pays } from 'app/shared/model/enumerations/pays.model';
import { getEntity, updateEntity, createEntity, reset } from './enterprise.reducer';

export const EnterpriseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const enterpriseEntity = useAppSelector(state => state.enterprise.entity);
  const loading = useAppSelector(state => state.enterprise.loading);
  const updating = useAppSelector(state => state.enterprise.updating);
  const updateSuccess = useAppSelector(state => state.enterprise.updateSuccess);
  const paysValues = Object.keys(Pays);

  const handleClose = () => {
    navigate('/enterprise' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    const entity = {
      ...enterpriseEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          country: 'CAMEROON',
          ...enterpriseEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mondodbApp.enterprise.home.createOrEditLabel" data-cy="EnterpriseCreateUpdateHeading">
            <Translate contentKey="mondodbApp.enterprise.home.createOrEditLabel">Create or edit a Enterprise</Translate>
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
                  id="enterprise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mondodbApp.enterprise.companyName')}
                id="enterprise-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="companyNameLabel">
                <Translate contentKey="mondodbApp.enterprise.help.companyName" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('mondodbApp.enterprise.businessRegisterNumber')}
                id="enterprise-businessRegisterNumber"
                name="businessRegisterNumber"
                data-cy="businessRegisterNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.enterprise.uniqueIdentificationNumber')}
                id="enterprise-uniqueIdentificationNumber"
                name="uniqueIdentificationNumber"
                data-cy="uniqueIdentificationNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.enterprise.businessDomicile')}
                id="enterprise-businessDomicile"
                name="businessDomicile"
                data-cy="businessDomicile"
                type="text"
              />
              <ValidatedField
                label={translate('mondodbApp.enterprise.email')}
                id="enterprise-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.enterprise.businessPhone')}
                id="enterprise-businessPhone"
                name="businessPhone"
                data-cy="businessPhone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mondodbApp.enterprise.country')}
                id="enterprise-country"
                name="country"
                data-cy="country"
                type="select"
              >
                {paysValues.map(pays => (
                  <option value={pays} key={pays}>
                    {translate('mondodbApp.Pays.' + pays)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('mondodbApp.enterprise.city')} id="enterprise-city" name="city" data-cy="city" type="text" />
              <ValidatedBlobField
                label={translate('mondodbApp.enterprise.businessLogo')}
                id="enterprise-businessLogo"
                name="businessLogo"
                data-cy="businessLogo"
                isImage
                accept="image/*"
                validate={{}}
              />
              <ValidatedBlobField
                label={translate('mondodbApp.enterprise.mapLocator')}
                id="enterprise-mapLocator"
                name="mapLocator"
                data-cy="mapLocator"
                isImage
                accept="image/*"
                validate={{}}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/enterprise" replace color="info">
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

export default EnterpriseUpdate;
