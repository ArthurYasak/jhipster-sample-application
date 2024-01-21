import { ITestEntity } from 'app/shared/model/test-entity.model';

export interface ITestCreator {
  id?: number;
  testsAmount?: number | null;
  testEntities?: ITestEntity[] | null;
}

export const defaultValue: Readonly<ITestCreator> = {};
