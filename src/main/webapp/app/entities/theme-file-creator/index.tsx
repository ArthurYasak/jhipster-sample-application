import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ThemeFileCreator from './theme-file-creator';
import ThemeFileCreatorDetail from './theme-file-creator-detail';
import ThemeFileCreatorUpdate from './theme-file-creator-update';
import ThemeFileCreatorDeleteDialog from './theme-file-creator-delete-dialog';

const ThemeFileCreatorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ThemeFileCreator />} />
    <Route path="new" element={<ThemeFileCreatorUpdate />} />
    <Route path=":id">
      <Route index element={<ThemeFileCreatorDetail />} />
      <Route path="edit" element={<ThemeFileCreatorUpdate />} />
      <Route path="delete" element={<ThemeFileCreatorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ThemeFileCreatorRoutes;
