import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './statistic-generator.reducer';

export const StatisticGeneratorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const statisticGeneratorEntity = useAppSelector(state => state.statisticGenerator.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="statisticGeneratorDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.statisticGenerator.detail.title">StatisticGenerator</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{statisticGeneratorEntity.id}</dd>
          <dt>
            <span id="generatedReportsAmount">
              <Translate contentKey="jhipsterSampleApplicationApp.statisticGenerator.generatedReportsAmount">
                Generated Reports Amount
              </Translate>
            </span>
          </dt>
          <dd>{statisticGeneratorEntity.generatedReportsAmount}</dd>
        </dl>
        <Button tag={Link} to="/statistic-generator" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/statistic-generator/${statisticGeneratorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StatisticGeneratorDetail;
