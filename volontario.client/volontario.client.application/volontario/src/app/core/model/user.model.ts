import { UserRole } from 'src/app/core/model/user-role.model';
import { VolunteerData } from 'src/app/core/model/volunteer-data.model';

export class User {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public domainEmailAddress: string,
    public contactEmailAddress: string,
    public phoneNumber: string,
    public role: UserRole,
    public volunteerData: VolunteerData
  ) {}

  public static fromPayload(payload?: any): User {
    return new User(
      payload?.id,
      payload?.firstName,
      payload?.lastName,
      payload?.domainEmailAddress,
      payload?.contactEmailAddress,
      payload?.phoneNumber,
      UserRole.fromPayload(payload?.role),
      VolunteerData.fromPayload(payload?.volunteerData)
    );
  }
}
