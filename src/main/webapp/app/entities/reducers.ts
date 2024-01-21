import materialsFile from 'app/entities/materials-file/materials-file.reducer';
import materialsFileLoader from 'app/entities/materials-file-loader/materials-file-loader.reducer';
import themeFile from 'app/entities/theme-file/theme-file.reducer';
import themeFileCreator from 'app/entities/theme-file-creator/theme-file-creator.reducer';
import testEntity from 'app/entities/test-entity/test-entity.reducer';
import testLoader from 'app/entities/test-loader/test-loader.reducer';
import testCreator from 'app/entities/test-creator/test-creator.reducer';
import tester from 'app/entities/tester/tester.reducer';
import testUser from 'app/entities/test-user/test-user.reducer';
import reportSender from 'app/entities/report-sender/report-sender.reducer';
import statisticGenerator from 'app/entities/statistic-generator/statistic-generator.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  materialsFile,
  materialsFileLoader,
  themeFile,
  themeFileCreator,
  testEntity,
  testLoader,
  testCreator,
  tester,
  testUser,
  reportSender,
  statisticGenerator,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
