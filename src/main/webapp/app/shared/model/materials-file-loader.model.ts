import { IMaterialsFile } from 'app/shared/model/materials-file.model';

export interface IMaterialsFileLoader {
  id?: number;
  filesAmount?: number | null;
  materialsFiles?: IMaterialsFile[] | null;
}

export const defaultValue: Readonly<IMaterialsFileLoader> = {};
