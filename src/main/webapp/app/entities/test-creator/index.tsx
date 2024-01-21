import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCreator from './test-creator';
import TestCreatorDetail from './test-creator-detail';
import TestCreatorUpdate from './test-creator-update';
import TestCreatorDeleteDialog from './test-creator-delete-dialog';

const TestCreatorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCreator />} />
    <Route path="new" element={<TestCreatorUpdate />} />
    <Route path=":id">
      <Route index element={<TestCreatorDetail />} />
      <Route path="edit" element={<TestCreatorUpdate />} />
      <Route path="delete" element={<TestCreatorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCreatorRoutes;
