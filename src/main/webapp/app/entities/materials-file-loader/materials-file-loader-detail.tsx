import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './materials-file-loader.reducer';

export const MaterialsFileLoaderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const materialsFileLoaderEntity = useAppSelector(state => state.materialsFileLoader.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="materialsFileLoaderDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.materialsFileLoader.detail.title">MaterialsFileLoader</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{materialsFileLoaderEntity.id}</dd>
          <dt>
            <span id="filesAmount">
              <Translate contentKey="jhipsterSampleApplicationApp.materialsFileLoader.filesAmount">Files Amount</Translate>
            </span>
          </dt>
          <dd>{materialsFileLoaderEntity.filesAmount}</dd>
        </dl>
        <Button tag={Link} to="/materials-file-loader" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/materials-file-loader/${materialsFileLoaderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MaterialsFileLoaderDetail;
