import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './social-charges.reducer';

export const SocialChargesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const socialChargesEntity = useAppSelector(state => state.socialCharges.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="socialChargesDetailsHeading">
          <Translate contentKey="mondodbApp.socialCharges.detail.title">SocialCharges</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{socialChargesEntity.id}</dd>
          <dt>
            <span id="spentDate">
              <Translate contentKey="mondodbApp.socialCharges.spentDate">Spent Date</Translate>
            </span>
          </dt>
          <dd>
            {socialChargesEntity.spentDate ? (
              <TextFormat value={socialChargesEntity.spentDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="spentType">
              <Translate contentKey="mondodbApp.socialCharges.spentType">Spent Type</Translate>
            </span>
          </dt>
          <dd>{socialChargesEntity.spentType}</dd>
          <dt>
            <span id="statusCharges">
              <Translate contentKey="mondodbApp.socialCharges.statusCharges">Status Charges</Translate>
            </span>
          </dt>
          <dd>{socialChargesEntity.statusCharges}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="mondodbApp.socialCharges.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{socialChargesEntity.amount}</dd>
          <dt>
            <span id="commentText">
              <Translate contentKey="mondodbApp.socialCharges.commentText">Comment Text</Translate>
            </span>
          </dt>
          <dd>{socialChargesEntity.commentText}</dd>
          <dt>
            <Translate contentKey="mondodbApp.socialCharges.responsableDepense">Responsable Depense</Translate>
          </dt>
          <dd>{socialChargesEntity.responsableDepense ? socialChargesEntity.responsableDepense.email : ''}</dd>
          <dt>
            <Translate contentKey="mondodbApp.socialCharges.enterprise">Enterprise</Translate>
          </dt>
          <dd>{socialChargesEntity.enterprise ? socialChargesEntity.enterprise.companyName : ''}</dd>
        </dl>
        <Button tag={Link} to="/social-charges" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/social-charges/${socialChargesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SocialChargesDetail;
