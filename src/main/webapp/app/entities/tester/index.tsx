import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Tester from './tester';
import TesterDetail from './tester-detail';
import TesterUpdate from './tester-update';
import TesterDeleteDialog from './tester-delete-dialog';

const TesterRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Tester />} />
    <Route path="new" element={<TesterUpdate />} />
    <Route path=":id">
      <Route index element={<TesterDetail />} />
      <Route path="edit" element={<TesterUpdate />} />
      <Route path="delete" element={<TesterDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TesterRoutes;
