export enum UserRoleEnum {
  Volunteer = 1,
}

export class UserRole {
  constructor(public id: number, public name: string) {}

  public static fromPayload(payload?: any): UserRole {
    return new UserRole(payload?.id, payload?.name);
  }
}
