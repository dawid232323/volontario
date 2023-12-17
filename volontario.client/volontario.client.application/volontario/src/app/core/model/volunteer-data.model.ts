import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';

export class VolunteerData {
  constructor(
    public participationMotivation: string,
    public interestCategories: InterestCategoryDTO[]
  ) {}

  public static fromPayload(payload?: any): VolunteerData {
    return new VolunteerData(
      payload?.participationMotivation,
      payload?.interestCategories.map(InterestCategoryDTO.fromPayload)
    );
  }
}
