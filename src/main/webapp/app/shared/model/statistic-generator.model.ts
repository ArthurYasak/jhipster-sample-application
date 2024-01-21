import { ITestUser } from 'app/shared/model/test-user.model';

export interface IStatisticGenerator {
  id?: number;
  generatedReportsAmount?: number | null;
  testUsers?: ITestUser[] | null;
}

export const defaultValue: Readonly<IStatisticGenerator> = {};
