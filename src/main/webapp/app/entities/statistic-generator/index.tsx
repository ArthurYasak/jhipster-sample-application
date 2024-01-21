import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StatisticGenerator from './statistic-generator';
import StatisticGeneratorDetail from './statistic-generator-detail';
import StatisticGeneratorUpdate from './statistic-generator-update';
import StatisticGeneratorDeleteDialog from './statistic-generator-delete-dialog';

const StatisticGeneratorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StatisticGenerator />} />
    <Route path="new" element={<StatisticGeneratorUpdate />} />
    <Route path=":id">
      <Route index element={<StatisticGeneratorDetail />} />
      <Route path="edit" element={<StatisticGeneratorUpdate />} />
      <Route path="delete" element={<StatisticGeneratorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StatisticGeneratorRoutes;
