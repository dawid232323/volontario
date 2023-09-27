import { ApplicationDetails } from 'src/app/core/model/application.model';

export enum ApplicationActionTypeEnum {
  Accept,
  Reject,
  Awaiting,
  Reserve_list,
}

export interface ApplicationActionIf {
  application: ApplicationDetails;
  actionType: ApplicationActionTypeEnum;
  actionReason?: string;
}
