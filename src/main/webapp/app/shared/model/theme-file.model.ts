import { IMaterialsFile } from 'app/shared/model/materials-file.model';
import { IThemeFileCreator } from 'app/shared/model/theme-file-creator.model';
import { ITestEntity } from 'app/shared/model/test-entity.model';

export interface IThemeFile {
  id?: number;
  theme?: string | null;
  materialsFile?: IMaterialsFile | null;
  themeFileCreator?: IThemeFileCreator | null;
  testEntity?: ITestEntity | null;
}

export const defaultValue: Readonly<IThemeFile> = {};
