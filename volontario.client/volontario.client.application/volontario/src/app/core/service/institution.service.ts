import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  Institution,
  InstitutionModelBuilder,
  InstitutionRegisterModel,
  RegisterInstitutionEmployeeDto,
} from 'src/app/core/model/institution.model';
import { catchError, map, Observable, using } from 'rxjs';
import { InstitutionContactPersonModel } from 'src/app/core/model/InstitutionContactPerson.model';
import { HttpHeaders, HttpParams } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { VerifyType } from 'src/app/features/institution-verify/institution-verify.const';
import { EndpointUrls } from 'src/app/utils/url.util';
import { SetPasswordInterface } from 'src/app/core/interface/authorization.interface';
import { InstitutionWorker, User } from 'src/app/core/model/user.model';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';

export enum InstitutionWorkerRoleChangeTypeEnum {
  MarkAsEmployee,
  MarkAsAdmin,
}

@Injectable({ providedIn: 'root' })
export class InstitutionService {
  constructor(private restService: VolontarioRestService) {}

  public createInstitution(
    institutionModel: InstitutionRegisterModel
  ): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options: HttpOptionsInterface = {
      headers: headers,
    };
    return this.restService.post(
      EndpointUrls.institutionRegister,
      institutionModel
    );
  }

  public verifyInstitution(
    token: string,
    operationType?: VerifyType
  ): Observable<any> {
    const params = new HttpParams({ fromObject: { token: token } });
    const options: HttpOptionsInterface = {
      params: params,
    };
    return this.restService.post(
      EndpointUrls.getInstitutionVerify(operationType!),
      {},
      options
    );
  }

  public registerContactPerson(
    token: string,
    registerIf: SetPasswordInterface
  ): Observable<any> {
    const params = new HttpParams({ fromObject: { t: token } });
    const options: HttpOptionsInterface = {
      params: params,
    };
    return this.restService.post(
      EndpointUrls.institutionRegisterContactPerson,
      registerIf,
      options
    );
  }

  public setNewEmployeePassword(
    institutionId: number,
    token: string,
    password: SetPasswordInterface
  ) {
    const params = new HttpParams({ fromObject: { t: token } });
    const url = EndpointUrls.getEmployeeRegisterUrl(institutionId);
    return this.restService.post(url, password, { params: params });
  }

  public getInstitutionDetails(institutionId: number): Observable<Institution> {
    return this.restService
      .get(EndpointUrls.institutionResource.concat(`/${institutionId}`))
      .pipe(map(result => Institution.fromPayload(result)));
  }

  public getInstitutionWorkers(
    institutionId: number
  ): Observable<InstitutionWorker[]> {
    return this.restService
      .get(EndpointUrls.institutionWorkers.concat(`/${institutionId}`))
      .pipe(map(result => result.map(InstitutionWorker.fromPayload)));
  }

  public getAllInstitutionWorkers(): Observable<InstitutionWorker[]> {
    return this.restService
      .get(EndpointUrls.institutionWorkers)
      .pipe(map(result => result.map(InstitutionWorker.fromPayload)));
  }

  public changeInstitutionWorkerRole(
    workerId: number,
    institutionId: number,
    operationType: InstitutionWorkerRoleChangeTypeEnum
  ): Observable<void> {
    const finalUrl = EndpointUrls.getInstitutionWorkerRoleChangeUrl(
      workerId,
      institutionId,
      operationType
    );
    return this.restService.patch(finalUrl, {});
  }

  public registerInstitutionEmployee(
    employeeDto: RegisterInstitutionEmployeeDto
  ): Observable<User> {
    return this.restService
      .post(EndpointUrls.institutionRegisterEmployee, employeeDto)
      .pipe(map(result => User.fromPayload(result)));
  }

  public getInstitutionModelFromFormData(
    basicInfoValue: any,
    additionalInfoValue: any
  ): InstitutionRegisterModel {
    const contactPerson = new InstitutionContactPersonModel(
      basicInfoValue.registerPersonName,
      basicInfoValue.registerPersonLastName,
      basicInfoValue.registerPersonPhoneNumber,
      basicInfoValue.registerPersonEmail
    );
    const { institutionName, krsNumber, address } = basicInfoValue;
    const { institutionTags, institutionDescription, operationPlace } =
      additionalInfoValue;
    return InstitutionModelBuilder.builder()
      .setContactPerson(contactPerson)
      .setName(institutionName)
      .setKrsNumber(krsNumber)
      .setHeadquartersAddress(address)
      .setTags(institutionTags)
      .setDescription(institutionDescription)
      .setLocalization(operationPlace)
      .build();
  }

  public canManageInstitution(
    loggedUser: User,
    institution?: Institution
  ): boolean {
    if (loggedUser.hasUserRoles([UserRoleEnum.Moderator, UserRoleEnum.Admin])) {
      return true;
    }
    return (
      loggedUser.hasUserRole(UserRoleEnum.InstitutionAdmin) &&
      institution?.id === loggedUser.institution?.id
    );
  }

  public editInstitutionData(
    institutionId: number,
    data: Institution
  ): Observable<Institution> {
    return this.restService
      .put(EndpointUrls.institutionResource.concat(`/${institutionId}`), data)
      .pipe(map(result => Institution.fromPayload(result)));
  }
}
