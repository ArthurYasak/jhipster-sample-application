import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './theme-file-creator.reducer';

export const ThemeFileCreatorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const themeFileCreatorEntity = useAppSelector(state => state.themeFileCreator.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="themeFileCreatorDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.themeFileCreator.detail.title">ThemeFileCreator</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{themeFileCreatorEntity.id}</dd>
          <dt>
            <span id="filesAmount">
              <Translate contentKey="jhipsterSampleApplicationApp.themeFileCreator.filesAmount">Files Amount</Translate>
            </span>
          </dt>
          <dd>{themeFileCreatorEntity.filesAmount}</dd>
        </dl>
        <Button tag={Link} to="/theme-file-creator" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/theme-file-creator/${themeFileCreatorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ThemeFileCreatorDetail;
