import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ThemeFile from './theme-file';
import ThemeFileDetail from './theme-file-detail';
import ThemeFileUpdate from './theme-file-update';
import ThemeFileDeleteDialog from './theme-file-delete-dialog';

const ThemeFileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ThemeFile />} />
    <Route path="new" element={<ThemeFileUpdate />} />
    <Route path=":id">
      <Route index element={<ThemeFileDetail />} />
      <Route path="edit" element={<ThemeFileUpdate />} />
      <Route path="delete" element={<ThemeFileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ThemeFileRoutes;
