import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMaterialsFile } from 'app/shared/model/materials-file.model';
import { getEntities as getMaterialsFiles } from 'app/entities/materials-file/materials-file.reducer';
import { IThemeFileCreator } from 'app/shared/model/theme-file-creator.model';
import { getEntities as getThemeFileCreators } from 'app/entities/theme-file-creator/theme-file-creator.reducer';
import { ITestEntity } from 'app/shared/model/test-entity.model';
import { getEntities as getTestEntities } from 'app/entities/test-entity/test-entity.reducer';
import { IThemeFile } from 'app/shared/model/theme-file.model';
import { getEntity, updateEntity, createEntity, reset } from './theme-file.reducer';

export const ThemeFileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const materialsFiles = useAppSelector(state => state.materialsFile.entities);
  const themeFileCreators = useAppSelector(state => state.themeFileCreator.entities);
  const testEntities = useAppSelector(state => state.testEntity.entities);
  const themeFileEntity = useAppSelector(state => state.themeFile.entity);
  const loading = useAppSelector(state => state.themeFile.loading);
  const updating = useAppSelector(state => state.themeFile.updating);
  const updateSuccess = useAppSelector(state => state.themeFile.updateSuccess);

  const handleClose = () => {
    navigate('/theme-file' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMaterialsFiles({}));
    dispatch(getThemeFileCreators({}));
    dispatch(getTestEntities({}));
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
      ...themeFileEntity,
      ...values,
      materialsFile: materialsFiles.find(it => it.id.toString() === values.materialsFile.toString()),
      themeFileCreator: themeFileCreators.find(it => it.id.toString() === values.themeFileCreator.toString()),
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
          ...themeFileEntity,
          materialsFile: themeFileEntity?.materialsFile?.id,
          themeFileCreator: themeFileEntity?.themeFileCreator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.themeFile.home.createOrEditLabel" data-cy="ThemeFileCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.themeFile.home.createOrEditLabel">Create or edit a ThemeFile</Translate>
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
                  id="theme-file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.themeFile.theme')}
                id="theme-file-theme"
                name="theme"
                data-cy="theme"
                type="text"
              />
              <ValidatedField
                id="theme-file-materialsFile"
                name="materialsFile"
                data-cy="materialsFile"
                label={translate('jhipsterSampleApplicationApp.themeFile.materialsFile')}
                type="select"
              >
                <option value="" key="0" />
                {materialsFiles
                  ? materialsFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="theme-file-themeFileCreator"
                name="themeFileCreator"
                data-cy="themeFileCreator"
                label={translate('jhipsterSampleApplicationApp.themeFile.themeFileCreator')}
                type="select"
              >
                <option value="" key="0" />
                {themeFileCreators
                  ? themeFileCreators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/theme-file" replace color="info">
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

export default ThemeFileUpdate;
