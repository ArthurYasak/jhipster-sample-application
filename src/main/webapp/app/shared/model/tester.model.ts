import { ITestEntity } from 'app/shared/model/test-entity.model';

export interface ITester {
  id?: number;
  holdTests?: number | null;
  testEntities?: ITestEntity[] | null;
}

export const defaultValue: Readonly<ITester> = {};
