import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMaterialsFileLoader } from 'app/shared/model/materials-file-loader.model';
import { getEntities as getMaterialsFileLoaders } from 'app/entities/materials-file-loader/materials-file-loader.reducer';
import { IThemeFile } from 'app/shared/model/theme-file.model';
import { getEntities as getThemeFiles } from 'app/entities/theme-file/theme-file.reducer';
import { IMaterialsFile } from 'app/shared/model/materials-file.model';
import { getEntity, updateEntity, createEntity, reset } from './materials-file.reducer';

export const MaterialsFileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const materialsFileLoaders = useAppSelector(state => state.materialsFileLoader.entities);
  const themeFiles = useAppSelector(state => state.themeFile.entities);
  const materialsFileEntity = useAppSelector(state => state.materialsFile.entity);
  const loading = useAppSelector(state => state.materialsFile.loading);
  const updating = useAppSelector(state => state.materialsFile.updating);
  const updateSuccess = useAppSelector(state => state.materialsFile.updateSuccess);

  const handleClose = () => {
    navigate('/materials-file' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMaterialsFileLoaders({}));
    dispatch(getThemeFiles({}));
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

    const entity = {
      ...materialsFileEntity,
      ...values,
      materialsFileLoader: materialsFileLoaders.find(it => it.id.toString() === values.materialsFileLoader.toString()),
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
          ...materialsFileEntity,
          materialsFileLoader: materialsFileEntity?.materialsFileLoader?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.materialsFile.home.createOrEditLabel" data-cy="MaterialsFileCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.materialsFile.home.createOrEditLabel">
              Create or edit a MaterialsFile
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
                  id="materials-file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.materialsFile.materials')}
                id="materials-file-materials"
                name="materials"
                data-cy="materials"
                type="text"
              />
              <ValidatedField
                id="materials-file-materialsFileLoader"
                name="materialsFileLoader"
                data-cy="materialsFileLoader"
                label={translate('jhipsterSampleApplicationApp.materialsFile.materialsFileLoader')}
                type="select"
              >
                <option value="" key="0" />
                {materialsFileLoaders
                  ? materialsFileLoaders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/materials-file" replace color="info">
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

export default MaterialsFileUpdate;
