import { UserRole, UserRoleEnum } from 'src/app/core/model/user-role.model';
import { VolunteerData } from 'src/app/core/model/volunteer-data.model';
import { isNil } from 'lodash';

export class User {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public domainEmailAddress: string,
    public contactEmailAddress: string,
    public phoneNumber: string,
    public roles: UserRole[],
    public volunteerData: VolunteerData
  ) {}

  public hasUserRole(desiredRole: UserRoleEnum): boolean {
    return !isNil(this.roles.find(role => role.id === desiredRole));
  }

  public hasUserRoles(desiredRoles: UserRoleEnum[]): boolean {
    return desiredRoles.some(role => this.hasUserRole(role));
  }

  public static fromPayload(payload?: any): User {
    return new User(
      payload?.id,
      payload?.firstName,
      payload?.lastName,
      payload?.domainEmailAddress,
      payload?.contactEmailAddress,
      payload?.phoneNumber,
      payload?.roles,
      VolunteerData.fromPayload(payload?.volunteerData)
    );
  }
}
