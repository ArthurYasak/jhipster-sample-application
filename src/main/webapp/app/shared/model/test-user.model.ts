import { IReportSender } from 'app/shared/model/report-sender.model';
import { IStatisticGenerator } from 'app/shared/model/statistic-generator.model';
import { ITestEntity } from 'app/shared/model/test-entity.model';

export interface ITestUser {
  id?: number;
  marks?: number | null;
  reportSender?: IReportSender | null;
  statisticGenerator?: IStatisticGenerator | null;
  testEntities?: ITestEntity[] | null;
}

export const defaultValue: Readonly<ITestUser> = {};
