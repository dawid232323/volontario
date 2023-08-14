import { VerifyType } from 'src/app/features/institution-verify/institution-verify.const';
import { ApplicationActionTypeEnum } from 'src/app/core/interface/application.interface';
import {
  ActivatedRoute,
  Params,
  QueryParamsHandling,
  Router,
} from '@angular/router';
import { isNil } from 'lodash';

export class EndpointUrls {
  public static readonly login: string = '/login';
  public static readonly refreshToken: string = '/refresh/token';
  public static readonly volunteerResource: string = '/volunteer';
  public static readonly registerVolunteer: string =
    EndpointUrls.volunteerResource.concat('/register');
  public static readonly institutionResource: string = '/institution';
  public static readonly institutionRegister: string =
    EndpointUrls.institutionResource.concat('/register');
  public static readonly institutionRegisterContactPerson =
    EndpointUrls.institutionResource.concat('/register-contact-person');
  public static readonly interestCategories: string = '/interestCategories';
  public static readonly experienceLevels: string = '/experienceLevels';
  public static readonly userData: string = '/userData';
  public static getInstitutionVerify(operationType: VerifyType) {
    return this.institutionResource.concat('/', operationType);
  }
  public static readonly advertisementResource = '/offer';
  public static readonly advertisementDetails =
    EndpointUrls.advertisementResource.concat('/details');
  public static readonly advertisementSearch =
    EndpointUrls.advertisementResource.concat('/search');
  public static readonly advertisementType =
    EndpointUrls.advertisementResource.concat('/type');
  public static readonly advertisementBenefits =
    EndpointUrls.advertisementResource.concat('/benefit');
  public static readonly advertisementChangeVisibilityResource =
    EndpointUrls.advertisementResource.concat('/changeVisibility');
  public static readonly offerApplicationResource = '/application';
  public static readonly offerApplicationSearchResource =
    EndpointUrls.offerApplicationResource.concat('/search');
  public static readonly offerApplicationSearchDetailsResource =
    EndpointUrls.offerApplicationResource.concat('/searchDetails');
  public static readonly offerApplicationMarkStarred =
    EndpointUrls.offerApplicationResource.concat('/star');
  public static readonly offerApplicationMarkUnStarred =
    EndpointUrls.offerApplicationResource.concat('/unstar');
  public static readonly offerApplicationCheckState =
    EndpointUrls.offerApplicationResource.concat('/checkState');
  public static readonly userResource = '/user';
  public static readonly changeUserActiveStatusUrl =
    EndpointUrls.userResource.concat('/change-status');
  public static readonly changeUserRolesUrl =
    EndpointUrls.userResource.concat('/change-roles');
  public static readonly changeUserPassword = '/change-password';

  public static readonly unauthorizedUrls = new Set<string>([
    EndpointUrls.institutionRegister,
    EndpointUrls.getInstitutionVerify(VerifyType.ACCEPT),
    EndpointUrls.getInstitutionVerify(VerifyType.REJECT),
    EndpointUrls.login,
    EndpointUrls.registerVolunteer,
    EndpointUrls.refreshToken,
  ]);

  public static getApplicationStateCheckUrl(
    applicationId: number,
    operationType: ApplicationActionTypeEnum
  ): string {
    if (operationType === ApplicationActionTypeEnum.Accept) {
      return EndpointUrls.offerApplicationResource.concat(
        '/accept',
        `/${applicationId}`
      );
    }
    return EndpointUrls.offerApplicationResource.concat(
      '/decline',
      `/${applicationId}`
    );
  }
}

export function updateActiveUrl(
  router: Router,
  activatedRoute: ActivatedRoute,
  params: Params,
  handling?: QueryParamsHandling
) {
  router.navigate([], {
    relativeTo: activatedRoute,
    queryParams: params,
    queryParamsHandling: handling,
  });
}
