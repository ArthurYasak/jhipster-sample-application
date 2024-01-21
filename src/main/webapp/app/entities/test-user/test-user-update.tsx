import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReportSender } from 'app/shared/model/report-sender.model';
import { getEntities as getReportSenders } from 'app/entities/report-sender/report-sender.reducer';
import { IStatisticGenerator } from 'app/shared/model/statistic-generator.model';
import { getEntities as getStatisticGenerators } from 'app/entities/statistic-generator/statistic-generator.reducer';
import { ITestEntity } from 'app/shared/model/test-entity.model';
import { getEntities as getTestEntities } from 'app/entities/test-entity/test-entity.reducer';
import { ITestUser } from 'app/shared/model/test-user.model';
import { getEntity, updateEntity, createEntity, reset } from './test-user.reducer';

export const TestUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reportSenders = useAppSelector(state => state.reportSender.entities);
  const statisticGenerators = useAppSelector(state => state.statisticGenerator.entities);
  const testEntities = useAppSelector(state => state.testEntity.entities);
  const testUserEntity = useAppSelector(state => state.testUser.entity);
  const loading = useAppSelector(state => state.testUser.loading);
  const updating = useAppSelector(state => state.testUser.updating);
  const updateSuccess = useAppSelector(state => state.testUser.updateSuccess);

  const handleClose = () => {
    navigate('/test-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReportSenders({}));
    dispatch(getStatisticGenerators({}));
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
    if (values.marks !== undefined && typeof values.marks !== 'number') {
      values.marks = Number(values.marks);
    }

    const entity = {
      ...testUserEntity,
      ...values,
      reportSender: reportSenders.find(it => it.id.toString() === values.reportSender.toString()),
      statisticGenerator: statisticGenerators.find(it => it.id.toString() === values.statisticGenerator.toString()),
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
          ...testUserEntity,
          reportSender: testUserEntity?.reportSender?.id,
          statisticGenerator: testUserEntity?.statisticGenerator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.testUser.home.createOrEditLabel" data-cy="TestUserCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.testUser.home.createOrEditLabel">Create or edit a TestUser</Translate>
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
                  id="test-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.testUser.marks')}
                id="test-user-marks"
                name="marks"
                data-cy="marks"
                type="text"
              />
              <ValidatedField
                id="test-user-reportSender"
                name="reportSender"
                data-cy="reportSender"
                label={translate('jhipsterSampleApplicationApp.testUser.reportSender')}
                type="select"
              >
                <option value="" key="0" />
                {reportSenders
                  ? reportSenders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="test-user-statisticGenerator"
                name="statisticGenerator"
                data-cy="statisticGenerator"
                label={translate('jhipsterSampleApplicationApp.testUser.statisticGenerator')}
                type="select"
              >
                <option value="" key="0" />
                {statisticGenerators
                  ? statisticGenerators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-user" replace color="info">
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

export default TestUserUpdate;
