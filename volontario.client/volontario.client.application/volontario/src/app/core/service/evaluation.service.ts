import { Injectable } from '@angular/core';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';
import { map, Observable } from 'rxjs';
import {
  EvaluationDto,
  OffersToEvaluateIf,
  UserEvaluation,
} from 'src/app/core/model/evaluation.model';
import { EndpointUrls } from 'src/app/utils/url.util';

@Injectable({ providedIn: 'root' })
export class EvaluationService {
  constructor(private restService: VolontarioRestService) {}

  /**
   * Resolves list of offers that volunteer with given id participated in, that can be evaluated by logged institution worker
   */
  public getInstitutionOffersToRateVolunteer(
    institutionId: number,
    volunteerId: number
  ): Observable<OffersToEvaluateIf> {
    return this.restService
      .get(
        EndpointUrls.evaluationResource.concat(
          `/institution/${institutionId}/offers-for/${volunteerId}`
        )
      )
      .pipe(
        map(result => {
          return {
            canEvaluateUser:
              (result && result.offersLeftToRate?.length > 0) || false,
            offersToEvaluate:
              result.offersLeftToRate?.map((offer: { id: any; title: any }) => {
                return { offerId: offer.id, offerName: offer.title };
              }) || [],
          };
        })
      );
  }

  /**
   * Resolves list of offers that institution with given id created, that can be evaluated by logged volunteer
   */
  public getVolunteerOffersToRateInstitution(
    volunteerId: number
  ): Observable<OffersToEvaluateIf> {
    return this.restService
      .get(
        EndpointUrls.evaluationResource.concat(
          `/volunteer/${volunteerId}/offers`
        )
      )
      .pipe(
        map(result => {
          return {
            canEvaluateUser:
              (result && result.offersLeftToRate?.length > 0) || false,
            offersToEvaluate:
              result.offersLeftToRate?.map((offer: { id: any; title: any }) => {
                return { offerId: offer.id, offerName: offer.title };
              }) || [],
          };
        })
      );
  }

  public getInstitutionEvaluation(
    institutionId: number
  ): Observable<UserEvaluation> {
    return this.restService
      .get(
        EndpointUrls.evaluationResource.concat(
          `/institution-average-rating/${institutionId}`
        )
      )
      .pipe(map(result => UserEvaluation.fromInstitutionPayload(result)));
  }

  public getUserEvaluation(volunteerId: number): Observable<UserEvaluation> {
    return this.restService
      .get(
        EndpointUrls.evaluationResource.concat(
          `/volunteer-average-rating/${volunteerId}`
        )
      )
      .pipe(map(rating => UserEvaluation.fromVolunteerPayload(rating)));
  }

  public rateVolunteer(
    volunteerId: number,
    requestBody: EvaluationDto
  ): Observable<void> {
    return this.restService.post(
      EndpointUrls.evaluationResource.concat(`/volunteer/${volunteerId}`),
      requestBody
    );
  }

  public rateInstitution(
    institutionId: number,
    requestBody: EvaluationDto
  ): Observable<void> {
    return this.restService.post(
      EndpointUrls.evaluationResource.concat(`/institution/${institutionId}`),
      requestBody
    );
  }

  public editVolunteerRating(
    volunteerId: number,
    requestBody: EvaluationDto
  ): Observable<void> {
    return this.restService.patch(
      EndpointUrls.evaluationResource.concat(`/volunteer/${volunteerId}`),
      requestBody
    );
  }

  public editInstitutionRating(
    institutionId: number,
    requestBody: EvaluationDto
  ): Observable<void> {
    return this.restService.patch(
      EndpointUrls.evaluationResource.concat(`/institution/${institutionId}`),
      requestBody
    );
  }

  public deleteVolunteerRating(
    volunteerId: number,
    offerId: number
  ): Observable<void> {
    return this.restService.delete(
      EndpointUrls.evaluationResource.concat(
        '/volunteer/' + volunteerId + '/' + offerId
      )
    );
  }

  public deleteInstitutionRating(
    volunteerId: number,
    offerId: number
  ): Observable<void> {
    return this.restService.delete(
      EndpointUrls.evaluationResource.concat(
        '/institution/' + offerId + '/' + volunteerId
      )
    );
  }
}
