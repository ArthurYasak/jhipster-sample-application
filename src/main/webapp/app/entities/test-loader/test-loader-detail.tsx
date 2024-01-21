import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-loader.reducer';

export const TestLoaderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testLoaderEntity = useAppSelector(state => state.testLoader.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testLoaderDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.testLoader.detail.title">TestLoader</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{testLoaderEntity.id}</dd>
          <dt>
            <span id="testsAmount">
              <Translate contentKey="jhipsterSampleApplicationApp.testLoader.testsAmount">Tests Amount</Translate>
            </span>
          </dt>
          <dd>{testLoaderEntity.testsAmount}</dd>
        </dl>
        <Button tag={Link} to="/test-loader" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-loader/${testLoaderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestLoaderDetail;
