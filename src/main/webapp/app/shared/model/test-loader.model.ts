import { ITestEntity } from 'app/shared/model/test-entity.model';

export interface ITestLoader {
  id?: number;
  testsAmount?: number | null;
  testEntities?: ITestEntity[] | null;
}

export const defaultValue: Readonly<ITestLoader> = {};
