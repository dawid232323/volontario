import { ApplicationDetails } from 'src/app/core/model/application.model';

export enum ApplicationActionTypeEnum {
  Accept,
  Reject,
}

export interface ApplicationActionIf {
  application: ApplicationDetails;
  actionType: ApplicationActionTypeEnum;
}
