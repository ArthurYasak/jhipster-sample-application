import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestEntity from './test-entity';
import TestEntityDetail from './test-entity-detail';
import TestEntityUpdate from './test-entity-update';
import TestEntityDeleteDialog from './test-entity-delete-dialog';

const TestEntityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestEntity />} />
    <Route path="new" element={<TestEntityUpdate />} />
    <Route path=":id">
      <Route index element={<TestEntityDetail />} />
      <Route path="edit" element={<TestEntityUpdate />} />
      <Route path="delete" element={<TestEntityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestEntityRoutes;
