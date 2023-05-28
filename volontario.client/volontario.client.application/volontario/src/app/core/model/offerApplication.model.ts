import { User } from 'src/app/core/model/user.model';
import { AdvertisementDto } from 'src/app/core/model/advertisement.model';

/**
 * Dto that transfers data for submitting application
 */
export class OfferApplicationModelDto {
  constructor(
    public volunteerId: number,
    public offerId: number,
    public participationMotivation: string
  ) {}

  public static fromApplyForm(
    userId: number,
    offerId: number,
    motivation: string
  ): OfferApplicationModelDto {
    return new OfferApplicationModelDto(userId, offerId, motivation);
  }
}

export class OfferApplicationStateModel {
  constructor(public id: number, public name: string) {}

  public static fromPayload(payload?: any): OfferApplicationStateModel {
    return new OfferApplicationStateModel(payload?.id, payload?.name);
  }
}

export class OfferApplicationModel {
  constructor(
    public id: number,
    public volunteer: User,
    public offer: AdvertisementDto,
    public state: OfferApplicationStateModel,
    public participationMotivation: string,
    public isStarred: boolean
  ) {}

  public static fromPayload(payload?: any): OfferApplicationModel {
    return new OfferApplicationModel(
      payload?.id,
      User.fromPayload(payload?.volunteer),
      AdvertisementDto.fromPayload(payload?.offer),
      OfferApplicationStateModel.fromPayload(payload?.state),
      payload?.participationMotivation,
      payload?.isStarred
    );
  }
}
