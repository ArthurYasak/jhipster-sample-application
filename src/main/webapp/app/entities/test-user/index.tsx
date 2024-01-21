import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestUser from './test-user';
import TestUserDetail from './test-user-detail';
import TestUserUpdate from './test-user-update';
import TestUserDeleteDialog from './test-user-delete-dialog';

const TestUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestUser />} />
    <Route path="new" element={<TestUserUpdate />} />
    <Route path=":id">
      <Route index element={<TestUserDetail />} />
      <Route path="edit" element={<TestUserUpdate />} />
      <Route path="delete" element={<TestUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestUserRoutes;
