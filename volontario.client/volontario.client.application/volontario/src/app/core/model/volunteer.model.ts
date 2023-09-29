export class VolunteerRegisterDTO {
  constructor(
    public firstName: string,
    public lastName: string,
    public password: string,
    public domainEmail: string,
    public contactEmail: string,
    public participationMotivation: string,
    public experienceId: number,
    public interestCategoriesIds: number[],
    public fieldOfStudy: string | null,
    public phoneNumber?: string | null
  ) {}

  public static fromPayload(payload: any): VolunteerRegisterDTO {
    return new VolunteerRegisterDTO(
      payload?.firstName,
      payload?.lastName,
      payload?.password,
      payload?.domainEmail,
      payload?.contactEmail,
      payload?.participationMotivation,
      payload?.experience,
      payload?.interestCategories,
      payload?.fieldOfStudy,
      payload?.phoneNumber
    );
  }
}
