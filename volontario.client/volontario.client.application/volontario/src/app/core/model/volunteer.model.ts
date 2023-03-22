import {InterestCategoryDTO} from './interestCategory.model';


export class VolunteerRegisterDTO {
  constructor(
    public firstName: string,
    public lastName: string,
    public password: string,
    public domainEmail: string,
    public contactEmail: string,
    public phoneNumber: string,
    public participationMotivation: string,
    public experience: string,
    public interestCategories: InterestCategoryDTO[]
  ) {
  }

  public static fromPayload(payload: any): VolunteerRegisterDTO {
    return new VolunteerRegisterDTO(
      payload?.firstName,
      payload?.lastName,
      payload?.password,
      payload?.domainEmail,
      payload?.contactEmail,
      payload?.phoneNumber,
      payload?.participationMotivation,
      payload?.experience,
      payload?.interestCategories
    );
  }
}
