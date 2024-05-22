import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pay-slip.reducer';

export const PaySlipDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paySlipEntity = useAppSelector(state => state.paySlip.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paySlipDetailsHeading">
          <Translate contentKey="mondodbApp.paySlip.detail.title">PaySlip</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paySlipEntity.id}</dd>
          <dt>
            <span id="netSalaryPay">
              <Translate contentKey="mondodbApp.paySlip.netSalaryPay">Net Salary Pay</Translate>
            </span>
            <UncontrolledTooltip target="netSalaryPay">
              <Translate contentKey="mondodbApp.paySlip.help.netSalaryPay" />
            </UncontrolledTooltip>
          </dt>
          <dd>{paySlipEntity.netSalaryPay}</dd>
          <dt>
            <span id="paySlipDate">
              <Translate contentKey="mondodbApp.paySlip.paySlipDate">Pay Slip Date</Translate>
            </span>
          </dt>
          <dd>
            {paySlipEntity.paySlipDate ? <TextFormat value={paySlipEntity.paySlipDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="uploadPaySlip">
              <Translate contentKey="mondodbApp.paySlip.uploadPaySlip">Upload Pay Slip</Translate>
            </span>
          </dt>
          <dd>
            {paySlipEntity.uploadPaySlip ? (
              <div>
                {paySlipEntity.uploadPaySlipContentType ? (
                  <a onClick={openFile(paySlipEntity.uploadPaySlipContentType, paySlipEntity.uploadPaySlip)}>
                    <img
                      src={`data:${paySlipEntity.uploadPaySlipContentType};base64,${paySlipEntity.uploadPaySlip}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {paySlipEntity.uploadPaySlipContentType}, {byteSize(paySlipEntity.uploadPaySlip)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="mondodbApp.paySlip.employee">Employee</Translate>
          </dt>
          <dd>{paySlipEntity.employee ? paySlipEntity.employee.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/pay-slip" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pay-slip/${paySlipEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaySlipDetail;
