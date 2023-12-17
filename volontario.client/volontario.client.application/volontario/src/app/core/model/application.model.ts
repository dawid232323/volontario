import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';

export class ApplicationBaseInfo {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public participationMotivation: string,
    public starred: boolean,
    public state: string,
    public offer?: AdvertisementPreview
  ) {}

  public static fromPayload(payload?: any): ApplicationBaseInfo {
    return new ApplicationBaseInfo(
      payload?.id,
      payload?.firstName,
      payload?.lastName,
      payload?.participationMotivation,
      payload?.starred,
      payload?.state,
      payload?.offer
    );
  }
}

export class ApplicationDetails {
  constructor(
    public id: number,
    public state: string,
    public firstName: string,
    public lastName: string,
    public contactEmail: string,
    public domainEmail: string,
    public phoneNumber: string,
    public participationMotivation: string,
    public interestCategories: InterestCategoryDTO[],
    public offerInfo: AdvertisementPreview,
    public starred: boolean,
    public assignedPersonId: number,
    public decisionReason: string,
    public volunteerId: number
  ) {}

  public static fromPayload(payload?: any): ApplicationDetails {
    return new ApplicationDetails(
      payload?.id,
      payload?.state,
      payload?.firstName,
      payload?.lastName,
      payload?.contactEmail,
      payload?.domainEmail,
      payload?.phoneNumber,
      payload?.participationMotivation,
      payload?.interestCategories?.map(InterestCategoryDTO.fromPayload),
      AdvertisementPreview.fromPayload(payload?.offerInfo),
      payload?.starred,
      payload?.assignedPersonId,
      payload?.decisionReason,
      payload?.volunteerId
    );
  }
}

export class ApplicationStateCheck {
  constructor(public applied: boolean, public state: string) {}

  public static fromPayload(payload?: any): ApplicationStateCheck {
    return new ApplicationStateCheck(payload?.applied, payload?.state);
  }
}

export enum ApplicationStateEnumName {
  Waiting = 'Oczekująca',
  UnderRecruitment = 'W trakcie rekrutacji',
  Rejected = 'Odrzucona',
  Reserve_list = 'Lista rezerwowa',
}

export interface ApplicationStateIf {
  stateId: number;
  stateName: ApplicationStateEnumName;
  serverQueryStateName: string;
}

export class ApplicationStates {
  static readonly Rejected: ApplicationStateIf = {
    stateId: 1,
    stateName: ApplicationStateEnumName.Rejected,
    serverQueryStateName: 'declined',
  };

  static readonly Waiting: ApplicationStateIf = {
    stateId: 2,
    stateName: ApplicationStateEnumName.Waiting,
    serverQueryStateName: 'awaiting',
  };

  static readonly UnderRecruitment: ApplicationStateIf = {
    stateId: 3,
    stateName: ApplicationStateEnumName.UnderRecruitment,
    serverQueryStateName: 'under recruitment',
  };

  static readonly ReserveList: ApplicationStateIf = {
    stateId: 4,
    stateName: ApplicationStateEnumName.Reserve_list,
    serverQueryStateName: 'reserve list',
  };

  static get allStates(): ApplicationStateIf[] {
    return [
      this.Rejected,
      this.Waiting,
      this.UnderRecruitment,
      this.ReserveList,
    ];
  }
}
