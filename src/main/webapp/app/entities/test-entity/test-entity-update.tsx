import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IThemeFile } from 'app/shared/model/theme-file.model';
import { getEntities as getThemeFiles } from 'app/entities/theme-file/theme-file.reducer';
import { ITestCreator } from 'app/shared/model/test-creator.model';
import { getEntities as getTestCreators } from 'app/entities/test-creator/test-creator.reducer';
import { ITestLoader } from 'app/shared/model/test-loader.model';
import { getEntities as getTestLoaders } from 'app/entities/test-loader/test-loader.reducer';
import { ITester } from 'app/shared/model/tester.model';
import { getEntities as getTesters } from 'app/entities/tester/tester.reducer';
import { ITestUser } from 'app/shared/model/test-user.model';
import { getEntities as getTestUsers } from 'app/entities/test-user/test-user.reducer';
import { ITestEntity } from 'app/shared/model/test-entity.model';
import { getEntity, updateEntity, createEntity, reset } from './test-entity.reducer';

export const TestEntityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const themeFiles = useAppSelector(state => state.themeFile.entities);
  const testCreators = useAppSelector(state => state.testCreator.entities);
  const testLoaders = useAppSelector(state => state.testLoader.entities);
  const testers = useAppSelector(state => state.tester.entities);
  const testUsers = useAppSelector(state => state.testUser.entities);
  const testEntityEntity = useAppSelector(state => state.testEntity.entity);
  const loading = useAppSelector(state => state.testEntity.loading);
  const updating = useAppSelector(state => state.testEntity.updating);
  const updateSuccess = useAppSelector(state => state.testEntity.updateSuccess);

  const handleClose = () => {
    navigate('/test-entity' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getThemeFiles({}));
    dispatch(getTestCreators({}));
    dispatch(getTestLoaders({}));
    dispatch(getTesters({}));
    dispatch(getTestUsers({}));
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
    if (values.result !== undefined && typeof values.result !== 'number') {
      values.result = Number(values.result);
    }

    const entity = {
      ...testEntityEntity,
      ...values,
      testUsers: mapIdList(values.testUsers),
      themeFile: themeFiles.find(it => it.id.toString() === values.themeFile.toString()),
      testCreator: testCreators.find(it => it.id.toString() === values.testCreator.toString()),
      testLoader: testLoaders.find(it => it.id.toString() === values.testLoader.toString()),
      tester: testers.find(it => it.id.toString() === values.tester.toString()),
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
          ...testEntityEntity,
          themeFile: testEntityEntity?.themeFile?.id,
          testCreator: testEntityEntity?.testCreator?.id,
          testLoader: testEntityEntity?.testLoader?.id,
          tester: testEntityEntity?.tester?.id,
          testUsers: testEntityEntity?.testUsers?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.testEntity.home.createOrEditLabel" data-cy="TestEntityCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.testEntity.home.createOrEditLabel">Create or edit a TestEntity</Translate>
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
                  id="test-entity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.testEntity.question')}
                id="test-entity-question"
                name="question"
                data-cy="question"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.testEntity.testPoints')}
                id="test-entity-testPoints"
                name="testPoints"
                data-cy="testPoints"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.testEntity.result')}
                id="test-entity-result"
                name="result"
                data-cy="result"
                type="text"
              />
              <ValidatedField
                id="test-entity-themeFile"
                name="themeFile"
                data-cy="themeFile"
                label={translate('jhipsterSampleApplicationApp.testEntity.themeFile')}
                type="select"
              >
                <option value="" key="0" />
                {themeFiles
                  ? themeFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="test-entity-testCreator"
                name="testCreator"
                data-cy="testCreator"
                label={translate('jhipsterSampleApplicationApp.testEntity.testCreator')}
                type="select"
              >
                <option value="" key="0" />
                {testCreators
                  ? testCreators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="test-entity-testLoader"
                name="testLoader"
                data-cy="testLoader"
                label={translate('jhipsterSampleApplicationApp.testEntity.testLoader')}
                type="select"
              >
                <option value="" key="0" />
                {testLoaders
                  ? testLoaders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="test-entity-tester"
                name="tester"
                data-cy="tester"
                label={translate('jhipsterSampleApplicationApp.testEntity.tester')}
                type="select"
              >
                <option value="" key="0" />
                {testers
                  ? testers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.testEntity.testUser')}
                id="test-entity-testUser"
                data-cy="testUser"
                type="select"
                multiple
                name="testUsers"
              >
                <option value="" key="0" />
                {testUsers
                  ? testUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-entity" replace color="info">
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

export default TestEntityUpdate;
