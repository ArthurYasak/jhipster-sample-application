import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './theme-file.reducer';

export const ThemeFileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const themeFileEntity = useAppSelector(state => state.themeFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="themeFileDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.themeFile.detail.title">ThemeFile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{themeFileEntity.id}</dd>
          <dt>
            <span id="theme">
              <Translate contentKey="jhipsterSampleApplicationApp.themeFile.theme">Theme</Translate>
            </span>
          </dt>
          <dd>{themeFileEntity.theme}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.themeFile.materialsFile">Materials File</Translate>
          </dt>
          <dd>{themeFileEntity.materialsFile ? themeFileEntity.materialsFile.materials : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.themeFile.themeFileCreator">Theme File Creator</Translate>
          </dt>
          <dd>{themeFileEntity.themeFileCreator ? themeFileEntity.themeFileCreator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/theme-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/theme-file/${themeFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ThemeFileDetail;
