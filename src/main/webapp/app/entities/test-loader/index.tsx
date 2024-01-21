import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestLoader from './test-loader';
import TestLoaderDetail from './test-loader-detail';
import TestLoaderUpdate from './test-loader-update';
import TestLoaderDeleteDialog from './test-loader-delete-dialog';

const TestLoaderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestLoader />} />
    <Route path="new" element={<TestLoaderUpdate />} />
    <Route path=":id">
      <Route index element={<TestLoaderDetail />} />
      <Route path="edit" element={<TestLoaderUpdate />} />
      <Route path="delete" element={<TestLoaderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestLoaderRoutes;
