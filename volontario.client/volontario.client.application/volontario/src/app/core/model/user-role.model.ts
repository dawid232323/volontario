export enum UserRoleEnum {
  InstitutionWorker = 1,
  InstitutionAdmin = 2,
  Volunteer = 3,
  Admin = 4,
  Moderator = 5,
}

export class UserRole {
  constructor(public id: number, public name: string) {}

  public static fromPayload(payload?: any): UserRole {
    return new UserRole(payload?.id, payload?.name);
  }
}
