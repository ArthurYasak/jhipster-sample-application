import { IThemeFile } from 'app/shared/model/theme-file.model';

export interface IThemeFileCreator {
  id?: number;
  filesAmount?: number | null;
  themeFiles?: IThemeFile[] | null;
}

export const defaultValue: Readonly<IThemeFileCreator> = {};
