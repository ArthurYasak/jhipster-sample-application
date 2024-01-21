import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-entity.reducer';

export const TestEntityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testEntityEntity = useAppSelector(state => state.testEntity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testEntityDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.testEntity.detail.title">TestEntity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{testEntityEntity.id}</dd>
          <dt>
            <span id="testPoints">
              <Translate contentKey="jhipsterSampleApplicationApp.testEntity.testPoints">Test Points</Translate>
            </span>
          </dt>
          <dd>{testEntityEntity.testPoints}</dd>
          <dt>
            <span id="result">
              <Translate contentKey="jhipsterSampleApplicationApp.testEntity.result">Result</Translate>
            </span>
          </dt>
          <dd>{testEntityEntity.result}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.testEntity.themeFile">Theme File</Translate>
          </dt>
          <dd>{testEntityEntity.themeFile ? testEntityEntity.themeFile.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.testEntity.testCreator">Test Creator</Translate>
          </dt>
          <dd>{testEntityEntity.testCreator ? testEntityEntity.testCreator.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.testEntity.testLoader">Test Loader</Translate>
          </dt>
          <dd>{testEntityEntity.testLoader ? testEntityEntity.testLoader.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.testEntity.tester">Tester</Translate>
          </dt>
          <dd>{testEntityEntity.tester ? testEntityEntity.tester.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.testEntity.testUser">Test User</Translate>
          </dt>
          <dd>
            {testEntityEntity.testUsers
              ? testEntityEntity.testUsers.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {testEntityEntity.testUsers && i === testEntityEntity.testUsers.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/test-entity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-entity/${testEntityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestEntityDetail;
