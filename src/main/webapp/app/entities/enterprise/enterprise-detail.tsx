import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './enterprise.reducer';

export const EnterpriseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const enterpriseEntity = useAppSelector(state => state.enterprise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="enterpriseDetailsHeading">
          <Translate contentKey="mondodbApp.enterprise.detail.title">Enterprise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.id}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="mondodbApp.enterprise.companyName">Company Name</Translate>
            </span>
            <UncontrolledTooltip target="companyName">
              <Translate contentKey="mondodbApp.enterprise.help.companyName" />
            </UncontrolledTooltip>
          </dt>
          <dd>{enterpriseEntity.companyName}</dd>
          <dt>
            <span id="businessRegisterNumber">
              <Translate contentKey="mondodbApp.enterprise.businessRegisterNumber">Business Register Number</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.businessRegisterNumber}</dd>
          <dt>
            <span id="uniqueIdentificationNumber">
              <Translate contentKey="mondodbApp.enterprise.uniqueIdentificationNumber">Unique Identification Number</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.uniqueIdentificationNumber}</dd>
          <dt>
            <span id="businessDomicile">
              <Translate contentKey="mondodbApp.enterprise.businessDomicile">Business Domicile</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.businessDomicile}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="mondodbApp.enterprise.email">Email</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.email}</dd>
          <dt>
            <span id="businessPhone">
              <Translate contentKey="mondodbApp.enterprise.businessPhone">Business Phone</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.businessPhone}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="mondodbApp.enterprise.country">Country</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.country}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="mondodbApp.enterprise.city">City</Translate>
            </span>
          </dt>
          <dd>{enterpriseEntity.city}</dd>
          <dt>
            <span id="businessLogo">
              <Translate contentKey="mondodbApp.enterprise.businessLogo">Business Logo</Translate>
            </span>
          </dt>
          <dd>
            {enterpriseEntity.businessLogo ? (
              <div>
                {enterpriseEntity.businessLogoContentType ? (
                  <a onClick={openFile(enterpriseEntity.businessLogoContentType, enterpriseEntity.businessLogo)}>
                    <img
                      src={`data:${enterpriseEntity.businessLogoContentType};base64,${enterpriseEntity.businessLogo}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {enterpriseEntity.businessLogoContentType}, {byteSize(enterpriseEntity.businessLogo)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="mapLocator">
              <Translate contentKey="mondodbApp.enterprise.mapLocator">Map Locator</Translate>
            </span>
          </dt>
          <dd>
            {enterpriseEntity.mapLocator ? (
              <div>
                {enterpriseEntity.mapLocatorContentType ? (
                  <a onClick={openFile(enterpriseEntity.mapLocatorContentType, enterpriseEntity.mapLocator)}>
                    <img
                      src={`data:${enterpriseEntity.mapLocatorContentType};base64,${enterpriseEntity.mapLocator}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {enterpriseEntity.mapLocatorContentType}, {byteSize(enterpriseEntity.mapLocator)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/enterprise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/enterprise/${enterpriseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EnterpriseDetail;
