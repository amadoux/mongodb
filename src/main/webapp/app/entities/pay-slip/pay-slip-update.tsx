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
import { IPaySlip } from 'app/shared/model/pay-slip.model';
import { getEntity, updateEntity, createEntity, reset } from './pay-slip.reducer';

export const PaySlipUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const employees = useAppSelector(state => state.employee.entities);
  const paySlipEntity = useAppSelector(state => state.paySlip.entity);
  const loading = useAppSelector(state => state.paySlip.loading);
  const updating = useAppSelector(state => state.paySlip.updating);
  const updateSuccess = useAppSelector(state => state.paySlip.updateSuccess);

  const handleClose = () => {
    navigate('/pay-slip' + location.search);
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
    if (values.netSalaryPay !== undefined && typeof values.netSalaryPay !== 'number') {
      values.netSalaryPay = Number(values.netSalaryPay);
    }
    values.paySlipDate = convertDateTimeToServer(values.paySlipDate);

    const entity = {
      ...paySlipEntity,
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
          paySlipDate: displayDefaultDateTime(),
        }
      : {
          ...paySlipEntity,
          paySlipDate: convertDateTimeFromServer(paySlipEntity.paySlipDate),
          employee: paySlipEntity?.employee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mondodbApp.paySlip.home.createOrEditLabel" data-cy="PaySlipCreateUpdateHeading">
            <Translate contentKey="mondodbApp.paySlip.home.createOrEditLabel">Create or edit a PaySlip</Translate>
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
                  id="pay-slip-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mondodbApp.paySlip.netSalaryPay')}
                id="pay-slip-netSalaryPay"
                name="netSalaryPay"
                data-cy="netSalaryPay"
                type="text"
              />
              <UncontrolledTooltip target="netSalaryPayLabel">
                <Translate contentKey="mondodbApp.paySlip.help.netSalaryPay" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('mondodbApp.paySlip.paySlipDate')}
                id="pay-slip-paySlipDate"
                name="paySlipDate"
                data-cy="paySlipDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedBlobField
                label={translate('mondodbApp.paySlip.uploadPaySlip')}
                id="pay-slip-uploadPaySlip"
                name="uploadPaySlip"
                data-cy="uploadPaySlip"
                isImage
                accept="image/*"
                validate={{}}
              />
              <ValidatedField
                id="pay-slip-employee"
                name="employee"
                data-cy="employee"
                label={translate('mondodbApp.paySlip.employee')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pay-slip" replace color="info">
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

export default PaySlipUpdate;
