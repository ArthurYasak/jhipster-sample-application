import { ITestUser } from 'app/shared/model/test-user.model';

export interface IReportSender {
  id?: number;
  emailList?: string | null;
  testUsers?: ITestUser[] | null;
}

export const defaultValue: Readonly<IReportSender> = {};
