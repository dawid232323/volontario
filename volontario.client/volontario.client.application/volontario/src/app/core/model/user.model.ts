import {
  UserRole,
  UserRoleEnum,
  UserRoles,
} from 'src/app/core/model/user-role.model';
import { VolunteerData } from 'src/app/core/model/volunteer-data.model';
import { isNil } from 'lodash';
import { Institution } from 'src/app/core/model/institution.model';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';

export interface UserEntitlementToSeePersonalInfoIf {
  isEntitledForPersonalData: boolean;
}

interface UserRoleIf {
  /**
   * Checks if user has given role.
   *
   * @param desiredRole role to be checked
   *
   * @returns true if user has argument role or false if he doesn't
   */
  hasUserRole(desiredRole: UserRoleEnum): boolean;

  /**
   * Checks if user has one of the required roles.
   *
   * @param desiredRoles list of roles to be checked
   *
   * @returns true if user has any of the given roles, false if user doesn't have any
   */
  hasUserRoles(desiredRoles: UserRoleEnum[]): boolean;
}

export class User implements UserRoleIf {
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
    public firstName?: string,
    public lastName?: string,
    public interestCategoriesIds?: number[],
    public experienceId?: number,
    public participationMotivation?: string,
    public fieldOfStudy?: string
  ) {}

  public static fromApplyFormVerification(formValue: any): PatchUserDto {
    return new PatchUserDto(formValue.contactEmail, formValue.phoneNumber);
  }

  public static fromEditDataForm(formValue: any): PatchUserDto {
    return new PatchUserDto(
      formValue?.contactEmailAddress,
      formValue?.phoneNumber,
      formValue?.firstName,
      formValue?.lastName,
      formValue?.interestCategories,
      formValue?.experienceLevel,
      formValue?.participationMotivation,
      formValue?.fieldOfStudy
    );
  }
}

export class AdministrativeUserDetails {
  public fullName: string;
  public rolesList: string;
  constructor(
    public userId: number,
    public firstName: string,
    public lastName: string,
    public userRoles: UserRole[],
    public verified: boolean
  ) {
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
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public role?: string,
    public institutionId?: number,
    public institutionName?: string
  ) {}

  public static fromPayload(payload: any): InstitutionWorker {
    return new InstitutionWorker(
      payload?.id,
      payload?.firstName,
      payload?.lastName,
      payload?.role,
      payload?.institutionId,
      payload?.institutionName
    );
  }

  public static fromUser(user: User): InstitutionWorker {
    return new InstitutionWorker(
      user.id,
      user.firstName,
      user.lastName,
      this.getInstitutionRelatedRole(user),
      user.institution?.id,
      user.institution?.name
    );
  }

  private static getInstitutionRelatedRole(user: User): string | undefined {
    return user.roles.find(
      role =>
        role.id === UserRoleEnum.InstitutionAdmin ||
        role.id === UserRoleEnum.InstitutionWorker
    )?.name;
  }
}

export class UserProfile implements UserRoleIf {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public contactEmailAddress: string,
    public phoneNumber: string,
    public userRoles: string[],
    public domainEmailAddress?: string,
    public participationMotivation?: string,
    public experienceLevel?: VolunteerExperience,
    public interestCategories?: InterestCategoryDTO[],
    public institutionId?: number,
    public institutionName?: string,
    public fieldOfStudy?: string,
    public interests?: string,
    public experienceDescription?: string
  ) {}

  public hasUserRole(desiredRole: UserRoleEnum): boolean {
    const desiredUserRole = UserRoles.getAllRoles().find(
      role => role.id === desiredRole
    );
    if (isNil(desiredRole)) {
      return false;
    }
    return !isNil(this.userRoles.find(role => role === desiredUserRole!.name));
  }

  public hasUserRoles(desiredRoles: UserRoleEnum[]): boolean {
    return desiredRoles.some(role => this.hasUserRole(role));
  }

  public static fromPayload(payload?: any): UserProfile {
    return new UserProfile(
      payload?.id,
      payload?.firstName,
      payload?.lastName,
      payload?.contactEmailAddress,
      payload?.phoneNumber,
      payload?.userRoles,
      payload?.domainEmailAddress,
      payload?.participationMotivation,
      VolunteerExperience.fromPayload(payload?.experienceLevel),
      payload?.interestCategories?.map(InterestCategoryDTO.fromPayload),
      payload?.institutionId,
      payload?.institutionName,
      payload?.fieldOfStudy,
      payload?.interests,
      payload?.experienceDescription
    );
  }
}
