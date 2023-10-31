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

export class UserRoles {
  static readonly InstitutionWorker = new UserRole(
    UserRoleEnum.InstitutionWorker,
    'Pracownik/czka instytucji'
  );
  static readonly InstitutionAdmin = new UserRole(
    UserRoleEnum.InstitutionAdmin,
    'Administrator(ka) instytucji'
  );
  static readonly Volunteer = new UserRole(
    UserRoleEnum.Volunteer,
    'Wolontariusz(ka)'
  );
  static readonly Admin = new UserRole(UserRoleEnum.Admin, 'Administrator(ka)');
  static readonly Moderator = new UserRole(UserRoleEnum.Moderator, 'Moderator(ka)');

  static getAllRoles(): UserRole[] {
    return [
      this.InstitutionWorker,
      this.InstitutionAdmin,
      this.Volunteer,
      this.Admin,
      this.Moderator,
    ];
  }
}
