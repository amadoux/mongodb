import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './contract.reducer';

export const ContractDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contractEntity = useAppSelector(state => state.contract.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contractDetailsHeading">
          <Translate contentKey="mondodbApp.contract.detail.title">Contract</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contractEntity.id}</dd>
          <dt>
            <span id="contractType">
              <Translate contentKey="mondodbApp.contract.contractType">Contract Type</Translate>
            </span>
            <UncontrolledTooltip target="contractType">
              <Translate contentKey="mondodbApp.contract.help.contractType" />
            </UncontrolledTooltip>
          </dt>
          <dd>{contractEntity.contractType}</dd>
          <dt>
            <span id="entryDate">
              <Translate contentKey="mondodbApp.contract.entryDate">Entry Date</Translate>
            </span>
          </dt>
          <dd>{contractEntity.entryDate ? <TextFormat value={contractEntity.entryDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="releaseDate">
              <Translate contentKey="mondodbApp.contract.releaseDate">Release Date</Translate>
            </span>
          </dt>
          <dd>
            {contractEntity.releaseDate ? <TextFormat value={contractEntity.releaseDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="statusContract">
              <Translate contentKey="mondodbApp.contract.statusContract">Status Contract</Translate>
            </span>
          </dt>
          <dd>{contractEntity.statusContract}</dd>
          <dt>
            <span id="uploadContract">
              <Translate contentKey="mondodbApp.contract.uploadContract">Upload Contract</Translate>
            </span>
          </dt>
          <dd>
            {contractEntity.uploadContract ? (
              <div>
                {contractEntity.uploadContractContentType ? (
                  <a onClick={openFile(contractEntity.uploadContractContentType, contractEntity.uploadContract)}>
                    <img
                      src={`data:${contractEntity.uploadContractContentType};base64,${contractEntity.uploadContract}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {contractEntity.uploadContractContentType}, {byteSize(contractEntity.uploadContract)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="mondodbApp.contract.employee">Employee</Translate>
          </dt>
          <dd>{contractEntity.employee ? contractEntity.employee.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/contract" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contract/${contractEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContractDetail;
