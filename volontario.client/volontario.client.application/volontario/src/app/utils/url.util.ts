import { VerifyType } from 'src/app/features/institution-verify/institution-verify.const';

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
  public static readonly offerApplicationResource = '/application';
  static readonly unauthorizedUrls = new Set<string>([
    EndpointUrls.institutionRegister,
    EndpointUrls.getInstitutionVerify(VerifyType.ACCEPT),
    EndpointUrls.getInstitutionVerify(VerifyType.REJECT),
    EndpointUrls.login,
    EndpointUrls.registerVolunteer,
    EndpointUrls.refreshToken,
  ]);
}
