import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import {
  InstitutionModel,
  InstitutionModelBuilder,
} from 'src/app/core/model/institution.model';
import { Observable } from 'rxjs';
import { InstitutionContactPersonModel } from 'src/app/core/model/InstitutionContactPerson.model';
import { HttpHeaders } from '@angular/common/http';
import { HttpOptionsInterface } from 'src/app/core/interface/httpOptions.interface';

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
      '/institution/register',
      institutionModel,
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
      basicInfoValue.registerPersonEmail,
      basicInfoValue.registerPersonPhoneNumber
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
