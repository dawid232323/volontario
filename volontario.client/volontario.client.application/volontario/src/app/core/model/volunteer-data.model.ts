import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';

export class VolunteerData {
  constructor(
    public participationMotivation: string,
    public experience: VolunteerExperience,
    public interestCategories: InterestCategoryDTO[]
  ) {}

  public static fromPayload(payload?: any): VolunteerData {
    return new VolunteerData(
      payload?.participationMotivation,
      VolunteerExperience.fromPayload(payload?.experience),
      payload?.interestCategories.map(InterestCategoryDTO.fromPayload)
    );
  }
}
