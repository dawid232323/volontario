import { UserRole, UserRoleEnum } from 'src/app/core/model/user-role.model';
import { VolunteerData } from 'src/app/core/model/volunteer-data.model';
import { isNil } from 'lodash';
import { Institution } from 'src/app/core/model/institution.model';

export class User {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public domainEmailAddress: string,
    public contactEmailAddress: string,
    public phoneNumber: string,
    public roles: UserRole[],
    public volunteerData?: VolunteerData,
    public institution?: Institution
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
      VolunteerData.fromPayload(payload?.volunteerData),
      Institution.fromPayload(payload?.institution)
    );
  }
}

export class PatchUserDto {
  constructor(
    public contactEmailAddress: string,
    public phoneNumber: string,
    public interestCategoriesIds?: number[],
    public experienceId?: number,
    public participationMotivation?: string
  ) {}

  public static fromApplyFormVerification(formValue: any): PatchUserDto {
    return new PatchUserDto(formValue.contactEmail, formValue.phoneNumber);
  }
}

export class AdministrativeUserDetails {
  public fullName: string;
  public rolesList: string;
  constructor(public userId: number, public firstName: string, public lastName: string, public userRoles: UserRole[], public verified: boolean) {
    this.fullName = this.firstName.concat(' ', this.lastName);
    this.rolesList = this.userRoles.map(role => role.name).join(', ');
  }

  public static fromPayload(payload: any): AdministrativeUserDetails {
    return new AdministrativeUserDetails(
      payload?.userId,
      payload?.firstName,
      payload?.lastName,
      payload?.userRoles?.map(UserRole.fromPayload),
      payload?.verified
    );
  }
}

export class InstitutionWorker {
  constructor(public id: number, public firstName: string, public lastName: string, public institutionId?: number, public institutionName?: string) {}

  public static fromPayload(payload: any): InstitutionWorker {
    return new InstitutionWorker(payload?.id, payload?.firstName, payload?.lastName, payload?.institutionId, payload?.institutionName);
  }

  public static fromUser(user: User): InstitutionWorker {
    return new InstitutionWorker(user.id, user.firstName, user.lastName, user.institution?.id, user.institution?.name);
  }
}
