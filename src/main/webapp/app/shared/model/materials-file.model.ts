import { IMaterialsFileLoader } from 'app/shared/model/materials-file-loader.model';
import { IThemeFile } from 'app/shared/model/theme-file.model';

export interface IMaterialsFile {
  id?: number;
  materials?: string | null;
  materialsFileLoader?: IMaterialsFileLoader | null;
  themeFile?: IThemeFile | null;
}

export const defaultValue: Readonly<IMaterialsFile> = {};
