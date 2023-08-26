import { VerifyType } from 'src/app/features/institution-verify/institution-verify.const';
import { ApplicationActionTypeEnum } from 'src/app/core/interface/application.interface';
import { ActivatedRoute, Params, QueryParamsHandling, Router } from '@angular/router';
import { isNil } from 'lodash';
import * as moment from 'moment';

export class EndpointUrls {
  public static readonly login: string = '/login';
  public static readonly refreshToken: string = '/refresh/token';
  public static readonly volunteerResource: string = '/volunteer';
  public static readonly registerVolunteer: string = EndpointUrls.volunteerResource.concat('/register');
  public static readonly institutionResource: string = '/institution';
  public static readonly institutionRegister: string = EndpointUrls.institutionResource.concat('/register');
  public static readonly institutionRegisterContactPerson = EndpointUrls.institutionResource.concat('/register-contact-person');
  public static readonly userData: string = '/userData';
  public static getInstitutionVerify(operationType: VerifyType) {
    return this.institutionResource.concat('/', operationType);
  }
  public static readonly advertisementResource = '/offer';
  public static readonly advertisementDetails = EndpointUrls.advertisementResource.concat('/details');
  public static readonly advertisementSearch = EndpointUrls.advertisementResource.concat('/search');
  public static readonly advertisementType = EndpointUrls.advertisementResource.concat('/type');
  public static readonly advertisementChangeVisibilityResource = EndpointUrls.advertisementResource.concat('/changeVisibility');
  public static readonly offerApplicationResource = '/application';
  public static readonly offerApplicationSearchResource = EndpointUrls.offerApplicationResource.concat('/search');
  public static readonly offerApplicationSearchDetailsResource = EndpointUrls.offerApplicationResource.concat('/searchDetails');
  public static readonly offerApplicationMarkStarred = EndpointUrls.offerApplicationResource.concat('/star');
  public static readonly offerApplicationMarkUnStarred = EndpointUrls.offerApplicationResource.concat('/unstar');
  public static readonly offerApplicationCheckState = EndpointUrls.offerApplicationResource.concat('/checkState');
  public static readonly userResource = '/user';
  public static readonly changeUserActiveStatusUrl = EndpointUrls.userResource.concat('/change-status');
  public static readonly changeUserRolesUrl = EndpointUrls.userResource.concat('/change-roles');
  public static readonly changeUserPassword = '/change-password';
  public static readonly interestCategoryResource = '/interest-category';
  public static readonly notUsedInterestCategories = EndpointUrls.interestCategoryResource.concat('/all-not-used');
  public static readonly benefitResource = '/benefit';
  public static notUsedBenefits = EndpointUrls.benefitResource.concat('/all-not-used');
  public static readonly experienceLevelResource = '/experience-level';
  public static readonly notUsedExpLevels = EndpointUrls.experienceLevelResource.concat('/all-not-used');
  public static readonly advertisementBenefits = EndpointUrls.benefitResource.concat('/all-used');
  public static readonly interestCategories: string = EndpointUrls.interestCategoryResource.concat('/all-used');
  public static readonly experienceLevels: string = EndpointUrls.experienceLevelResource.concat('/all-used');
  public static readonly interestCategorySoftDelete = EndpointUrls.interestCategoryResource.concat('/soft-delete');
  public static readonly benefitSoftDelete = EndpointUrls.benefitResource.concat('/soft-delete');
  public static readonly expLevelsSoftDelete = EndpointUrls.experienceLevelResource.concat('/soft-delete');
  public static readonly interestCategoryRevertDelete = EndpointUrls.interestCategoryResource.concat('/revert-delete');
  public static readonly benefitRevertDelete = EndpointUrls.benefitResource.concat('/revert-delete');
  public static readonly epxLevelRevertDelete = EndpointUrls.experienceLevelResource.concat('/revert-delete');

  public static readonly unauthorizedUrls = new Set<string>([
    EndpointUrls.institutionRegister,
    EndpointUrls.getInstitutionVerify(VerifyType.ACCEPT),
    EndpointUrls.getInstitutionVerify(VerifyType.REJECT),
    EndpointUrls.login,
    EndpointUrls.registerVolunteer,
    EndpointUrls.refreshToken,
  ]);

  public static getApplicationStateCheckUrl(applicationId: number, operationType: ApplicationActionTypeEnum): string {
    if (operationType === ApplicationActionTypeEnum.Accept) {
      return EndpointUrls.offerApplicationResource.concat('/accept', `/${applicationId}`);
    }
    return EndpointUrls.offerApplicationResource.concat('/decline', `/${applicationId}`);
  }
}

export function updateActiveUrl(router: Router, activatedRoute: ActivatedRoute, params: Params, handling?: QueryParamsHandling) {
  router.navigate([], {
    relativeTo: activatedRoute,
    queryParams: params,
    queryParamsHandling: handling,
  });
}

export function addQueryParamFromSet(paramId: string, fromSet: Set<number> | undefined, existingParams: Params) {
  if (!isNil(fromSet) && fromSet?.size !== 0) {
    existingParams[paramId] = Array.from(fromSet ?? new Set<number>()).join(',');
  }
}

export function addQueryParamFromNumber(paramId: string, fromNumber: number, existingParams: Params) {
  if (!isNil(fromNumber)) {
    existingParams[paramId] = fromNumber;
  }
}

export function addQueryParamFromString(paramId: string, fromString: string | null | undefined, existingParams: Params) {
  if (!isNil(fromString) && fromString.length !== 0) {
    existingParams[paramId] = fromString;
  }
}

export function addQueryParamFromDate(paramId: string, fromDate: Date | null | undefined, existingParams: Params) {
  if (!isNil(fromDate)) {
    existingParams[paramId] = moment(fromDate).format('YYYY-MM-DD');
  }
}
