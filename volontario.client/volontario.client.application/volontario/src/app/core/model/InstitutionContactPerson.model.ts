export class InstitutionContactPersonModel {
  constructor(
    public firstName: string,
    public lastName: string,
    public phoneNumber: string,
    public contactEmail: string
  ) {}

  public static fromPayload(payload?: any): InstitutionContactPersonModel {
    return new InstitutionContactPersonModel(
      payload?.firstName,
      payload?.lastName,
      payload?.phoneNumber,
      payload?.contactEmail
    );
  }
}
