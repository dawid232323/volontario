export interface OffersToEvaluateIf {
  canEvaluateUser: boolean;
  offersToEvaluate: EvaluationAvailableOffer[];
}

export interface EvaluationAvailableOffer {
  offerId: number;
  offerName: string;
}

export interface EvaluationDto {
  volunteerId: number;
  offerId: number;
  rating: number;
  ratingReason: string | undefined | null;
}

export interface OfferEvaluationIf {
  offerId: number;
  offerName: string;
  evaluatorId: number;
  evaluatorName: string;
  evaluationValue: number;
  evaluationComment: string | null | undefined;
}

export class UserOfferEvaluation implements OfferEvaluationIf {
  constructor(
    public userId: number,
    public offerId: number,
    public offerName: string,
    public institutionName: string,
    public contactPersonId: number,
    public contactPersonName: string,
    public evaluationValue: number,
    public evaluationComment: string | null | undefined
  ) {}

  public get evaluatorId(): number {
    return this.contactPersonId;
  }

  public get evaluatorName(): string {
    return this.contactPersonName;
  }

  public static fromPayload(payload?: any): UserOfferEvaluation {
    return new UserOfferEvaluation(
      payload?.volunteerId,
      payload?.offerId,
      payload?.offerName,
      payload?.institutionName,
      payload?.contactPersonId,
      payload?.contactPersonName,
      payload?.rating,
      payload?.ratingComment
    );
  }
}

export class InstitutionOfferEvaluation implements OfferEvaluationIf {
  constructor(
    public offerId: number,
    public offerName: string,
    public volunteerId: number,
    public volunteerName: string,
    public evaluationValue: number,
    public evaluationComment: string | null | undefined
  ) {}

  public static fromPayload(payload?: any): InstitutionOfferEvaluation {
    return new InstitutionOfferEvaluation(
      payload?.offerId,
      payload?.offerName,
      payload?.volunteerId,
      payload?.volunteerName,
      payload?.rating,
      payload?.ratingComment
    );
  }

  public get evaluatorId(): number {
    return this.volunteerId;
  }

  public get evaluatorName(): string {
    return this.volunteerName;
  }
}

export class UserEvaluation {
  constructor(
    public evaluationAverage: number,
    public evaluations: OfferEvaluationIf[]
  ) {}

  public static fromVolunteerPayload(payload?: any): UserEvaluation {
    return new UserEvaluation(
      payload?.averageRating,
      payload?.volunteerRatings?.map(UserOfferEvaluation.fromPayload)
    );
  }

  public static fromInstitutionPayload(payload?: any) {
    return new UserEvaluation(
      payload?.averageRating,
      payload?.institutionRatings?.map(InstitutionOfferEvaluation.fromPayload)
    );
  }
}
