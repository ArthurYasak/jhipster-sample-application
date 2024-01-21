import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStatisticGenerator } from 'app/shared/model/statistic-generator.model';
import { getEntity, updateEntity, createEntity, reset } from './statistic-generator.reducer';

export const StatisticGeneratorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const statisticGeneratorEntity = useAppSelector(state => state.statisticGenerator.entity);
  const loading = useAppSelector(state => state.statisticGenerator.loading);
  const updating = useAppSelector(state => state.statisticGenerator.updating);
  const updateSuccess = useAppSelector(state => state.statisticGenerator.updateSuccess);

  const handleClose = () => {
    navigate('/statistic-generator');
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
    if (values.generatedReportsAmount !== undefined && typeof values.generatedReportsAmount !== 'number') {
      values.generatedReportsAmount = Number(values.generatedReportsAmount);
    }

    const entity = {
      ...statisticGeneratorEntity,
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
          ...statisticGeneratorEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.statisticGenerator.home.createOrEditLabel" data-cy="StatisticGeneratorCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.statisticGenerator.home.createOrEditLabel">
              Create or edit a StatisticGenerator
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
                  id="statistic-generator-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.statisticGenerator.generatedReportsAmount')}
                id="statistic-generator-generatedReportsAmount"
                name="generatedReportsAmount"
                data-cy="generatedReportsAmount"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/statistic-generator" replace color="info">
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

export default StatisticGeneratorUpdate;
