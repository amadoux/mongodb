import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './absence.reducer';

export const AbsenceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const absenceEntity = useAppSelector(state => state.absence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="absenceDetailsHeading">
          <Translate contentKey="mondodbApp.absence.detail.title">Absence</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="mondodbApp.absence.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.startDate ? <TextFormat value={absenceEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="mondodbApp.absence.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.endDate ? <TextFormat value={absenceEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="numberDayAbsence">
              <Translate contentKey="mondodbApp.absence.numberDayAbsence">Number Day Absence</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.numberDayAbsence}</dd>
          <dt>
            <span id="typeAbsence">
              <Translate contentKey="mondodbApp.absence.typeAbsence">Type Absence</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.typeAbsence}</dd>
          <dt>
            <span id="confirmationAbsence">
              <Translate contentKey="mondodbApp.absence.confirmationAbsence">Confirmation Absence</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.confirmationAbsence}</dd>
          <dt>
            <span id="congeRestant">
              <Translate contentKey="mondodbApp.absence.congeRestant">Conge Restant</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.congeRestant}</dd>
          <dt>
            <Translate contentKey="mondodbApp.absence.employee">Employee</Translate>
          </dt>
          <dd>{absenceEntity.employee ? absenceEntity.employee.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/absence" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/absence/${absenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AbsenceDetail;
