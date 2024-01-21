import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-user.reducer';

export const TestUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testUserEntity = useAppSelector(state => state.testUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testUserDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.testUser.detail.title">TestUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{testUserEntity.id}</dd>
          <dt>
            <span id="marks">
              <Translate contentKey="jhipsterSampleApplicationApp.testUser.marks">Marks</Translate>
            </span>
          </dt>
          <dd>{testUserEntity.marks}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.testUser.reportSender">Report Sender</Translate>
          </dt>
          <dd>{testUserEntity.reportSender ? testUserEntity.reportSender.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.testUser.statisticGenerator">Statistic Generator</Translate>
          </dt>
          <dd>{testUserEntity.statisticGenerator ? testUserEntity.statisticGenerator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-user/${testUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestUserDetail;
