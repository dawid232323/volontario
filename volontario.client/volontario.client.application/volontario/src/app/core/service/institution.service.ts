import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  InstitutionModel,
  InstitutionModelBuilder,
} from 'src/app/core/model/institution.model';
import { Observable } from 'rxjs';
import { InstitutionContactPersonModel } from 'src/app/core/model/InstitutionContactPerson.model';
import { HttpHeaders, HttpParams } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';
import { VerifyType } from 'src/app/features/institution-verify/institution-verify.const';
import { EndpointUrls } from 'src/app/utils/url.util';
import { RegisterContactPersonInterface } from 'src/app/core/interface/authorization.interface';

@Injectable({ providedIn: 'root' })
export class InstitutionService {
  constructor(private restService: VolontarioRestService) {}

  public createInstitution(
    institutionModel: InstitutionModel
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
    registerIf: RegisterContactPersonInterface
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

  public getInstitutionModelFromFormData(
    basicInfoValue: any,
    additionalInfoValue: any
  ): InstitutionModel {
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
}
