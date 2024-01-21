import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './materials-file.reducer';

export const MaterialsFileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const materialsFileEntity = useAppSelector(state => state.materialsFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="materialsFileDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.materialsFile.detail.title">MaterialsFile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{materialsFileEntity.id}</dd>
          <dt>
            <span id="materials">
              <Translate contentKey="jhipsterSampleApplicationApp.materialsFile.materials">Materials</Translate>
            </span>
          </dt>
          <dd>{materialsFileEntity.materials}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.materialsFile.materialsFileLoader">Materials File Loader</Translate>
          </dt>
          <dd>{materialsFileEntity.materialsFileLoader ? materialsFileEntity.materialsFileLoader.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/materials-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/materials-file/${materialsFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MaterialsFileDetail;
