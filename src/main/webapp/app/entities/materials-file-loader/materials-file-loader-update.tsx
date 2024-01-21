import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMaterialsFileLoader } from 'app/shared/model/materials-file-loader.model';
import { getEntity, updateEntity, createEntity, reset } from './materials-file-loader.reducer';

export const MaterialsFileLoaderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const materialsFileLoaderEntity = useAppSelector(state => state.materialsFileLoader.entity);
  const loading = useAppSelector(state => state.materialsFileLoader.loading);
  const updating = useAppSelector(state => state.materialsFileLoader.updating);
  const updateSuccess = useAppSelector(state => state.materialsFileLoader.updateSuccess);

  const handleClose = () => {
    navigate('/materials-file-loader');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.filesAmount !== undefined && typeof values.filesAmount !== 'number') {
      values.filesAmount = Number(values.filesAmount);
    }

    const entity = {
      ...materialsFileLoaderEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...materialsFileLoaderEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.materialsFileLoader.home.createOrEditLabel" data-cy="MaterialsFileLoaderCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.materialsFileLoader.home.createOrEditLabel">
              Create or edit a MaterialsFileLoader
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="materials-file-loader-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.materialsFileLoader.filesAmount')}
                id="materials-file-loader-filesAmount"
                name="filesAmount"
                data-cy="filesAmount"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/materials-file-loader" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MaterialsFileLoaderUpdate;
