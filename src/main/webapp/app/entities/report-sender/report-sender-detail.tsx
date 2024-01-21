import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './report-sender.reducer';

export const ReportSenderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reportSenderEntity = useAppSelector(state => state.reportSender.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reportSenderDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.reportSender.detail.title">ReportSender</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reportSenderEntity.id}</dd>
          <dt>
            <span id="emailList">
              <Translate contentKey="jhipsterSampleApplicationApp.reportSender.emailList">Email List</Translate>
            </span>
          </dt>
          <dd>{reportSenderEntity.emailList}</dd>
        </dl>
        <Button tag={Link} to="/report-sender" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/report-sender/${reportSenderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReportSenderDetail;
