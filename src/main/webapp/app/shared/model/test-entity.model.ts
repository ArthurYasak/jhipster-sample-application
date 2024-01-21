import { IThemeFile } from 'app/shared/model/theme-file.model';
import { ITestCreator } from 'app/shared/model/test-creator.model';
import { ITestLoader } from 'app/shared/model/test-loader.model';
import { ITester } from 'app/shared/model/tester.model';
import { ITestUser } from 'app/shared/model/test-user.model';

export interface ITestEntity {
  id?: number;
  question?: string | null;
  testPoints?: string | null;
  result?: number | null;
  themeFile?: IThemeFile | null;
  testCreator?: ITestCreator | null;
  testLoader?: ITestLoader | null;
  tester?: ITester | null;
  testUsers?: ITestUser[] | null;
}

export const defaultValue: Readonly<ITestEntity> = {};
