import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
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
    public experienceLevel: VolunteerExperience,
    public participationMotivation: string,
    public interestCategories: InterestCategoryDTO[],
    public offerInfo: AdvertisementPreview,
    public starred: boolean,
    public assignedPersonId: number,
    public decisionReason: string
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
      VolunteerExperience.fromPayload(payload?.experienceLevel),
      payload?.participationMotivation,
      payload?.interestCategories?.map(InterestCategoryDTO.fromPayload),
      AdvertisementPreview.fromPayload(payload?.offerInfo),
      payload?.starred,
      payload?.assignedPersonId,
      payload?.decisionReason
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
  Accepted = 'Zaakceptowana',
  Rejected = 'Odrzucona',
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

  static readonly Accepted: ApplicationStateIf = {
    stateId: 3,
    stateName: ApplicationStateEnumName.Accepted,
    serverQueryStateName: 'accepted',
  };

  static get allStates(): ApplicationStateIf[] {
    return [this.Rejected, this.Waiting, this.Accepted];
  }
}
