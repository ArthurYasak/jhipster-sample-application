import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MaterialsFile from './materials-file';
import MaterialsFileLoader from './materials-file-loader';
import ThemeFile from './theme-file';
import ThemeFileCreator from './theme-file-creator';
import TestEntity from './test-entity';
import TestLoader from './test-loader';
import TestCreator from './test-creator';
import Tester from './tester';
import TestUser from './test-user';
import ReportSender from './report-sender';
import StatisticGenerator from './statistic-generator';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="materials-file/*" element={<MaterialsFile />} />
        <Route path="materials-file-loader/*" element={<MaterialsFileLoader />} />
        <Route path="theme-file/*" element={<ThemeFile />} />
        <Route path="theme-file-creator/*" element={<ThemeFileCreator />} />
        <Route path="test-entity/*" element={<TestEntity />} />
        <Route path="test-loader/*" element={<TestLoader />} />
        <Route path="test-creator/*" element={<TestCreator />} />
        <Route path="tester/*" element={<Tester />} />
        <Route path="test-user/*" element={<TestUser />} />
        <Route path="report-sender/*" element={<ReportSender />} />
        <Route path="statistic-generator/*" element={<StatisticGenerator />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
