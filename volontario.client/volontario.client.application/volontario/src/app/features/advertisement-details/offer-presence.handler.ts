import { Injectable, OnDestroy } from '@angular/core';
import { ApplicationStateEnumName } from 'src/app/core/model/application.model';
import { isNil } from 'lodash';
import { map, Observable, of } from 'rxjs';
import { AdvertisementService } from 'src/app/core/service/advertisement.service';
import {
  InstitutionVoluntaryPresenceModel,
  PresenceStateEnum,
  VolunteerPresenceModel,
} from 'src/app/core/model/offer-presence.model';
import { User } from 'src/app/core/model/user.model';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';

export interface InstitutionPresenceStateIf {
  hasNotAcceptedApplications: boolean;
  canChangeDecision: boolean;
  presenceState?: InstitutionVoluntaryPresenceModel;
}

export interface VolunteerPresenceStateIf {
  canChangeDecision: boolean;
  canConfirmPresence?: boolean;
  presenceState?: VolunteerPresenceModel;
}

@Injectable()
export class OfferPresenceHandler implements OnDestroy {
  private _isOfferPresenceReady?: boolean;

  constructor(private offerService: AdvertisementService) {}

  public resolveIsOfferPresenceReady(offerIf: number): Observable<boolean> {
    if (!isNil(this._isOfferPresenceReady)) {
      return of(<boolean>this._isOfferPresenceReady);
    }
    return this.offerService.isOfferReadyToConfirmPresence(offerIf).pipe(
      map(result => {
        this._isOfferPresenceReady = result.isOfferReadyToConfirmPresences;
        return result.isOfferReadyToConfirmPresences;
      })
    );
  }

  public canUserConfirmPresence(
    applicationState: string | undefined,
    canManageOffer: boolean
  ) {
    if (
      ApplicationStateEnumName.UnderRecruitment === applicationState &&
      this._isOfferPresenceReady
    ) {
      return true;
    }
    return canManageOffer && (this._isOfferPresenceReady || false);
  }

  public resolveInstitutionPresenceState(
    offerId: number
  ): Observable<InstitutionPresenceStateIf> {
    return this.offerService.getInstitutionVoluntaryPresenceState(offerId).pipe(
      map(result => {
        const presenceState: InstitutionPresenceStateIf = {
          canChangeDecision: false,
          hasNotAcceptedApplications: false,
        };
        if (result === false) {
          presenceState.hasNotAcceptedApplications = true;
        } else {
          presenceState.canChangeDecision = !result.wasPresenceConfirmed
            ? true
            : result.canDecisionBeChanged;
          presenceState.presenceState = result;
        }
        return presenceState;
      })
    );
  }

  public resolveVolunteerPresenceState(
    offerId: number,
    userId: number
  ): Observable<VolunteerPresenceStateIf> {
    return this.offerService.getVolunteerPresenceState(offerId, userId).pipe(
      map(result => {
        if (result === false) {
          return { canChangeDecision: false, canConfirmPresence: false };
        }
        return {
          canChangeDecision:
            result.confirmationState === PresenceStateEnum.Unresolved
              ? true
              : result.canDecisionBeChanged,
          presenceState: result,
        };
      })
    );
  }

  public resolveInstitutionConfirmButtonLabel(
    canChangeDecision: boolean,
    presenceState?: InstitutionVoluntaryPresenceModel
  ): string {
    if (presenceState?.wasPresenceConfirmed && canChangeDecision) {
      return 'Zmień obecność wolontariuszy';
    }
    return 'Potwierdź udział wolontariuszy';
  }

  public resolveVolunteerConfirmButtonLabel(
    canChangeDecision: boolean,
    presenceState?: VolunteerPresenceModel
  ): string {
    if (this.hasVolunteerConfirmedPresence(canChangeDecision, presenceState)) {
      return 'Zmień swoją obecność';
    }
    return 'Potwierdź swój udział';
  }

  public resolveDecisionChangeLabel(
    canConfirmPresence: boolean,
    canChangePresenceDecision: boolean,
    canManageOffer: boolean,
    loggedUser: User,
    volunteerPresenceState?: VolunteerPresenceModel,
    institutionPresenceState?: InstitutionVoluntaryPresenceModel
  ): string | null {
    if (!this._isOfferPresenceReady || !canConfirmPresence) {
      return null;
    }
    if (!canChangePresenceDecision && canManageOffer) {
      return 'Nie możesz już zmienić decyzji dotyczącej udziału wolontariuszy w wolontariacie';
    }
    if (!canChangePresenceDecision) {
      return `Nie możesz już zmienić swojej decyzji dotyczącej udziału w wolontariacie. Status twojej decyzji to: <b>${volunteerPresenceState?.confirmationState}</b>`;
    }
    if (
      loggedUser.hasUserRoles([
        UserRoleEnum.InstitutionWorker,
        UserRoleEnum.InstitutionAdmin,
      ])
    ) {
      return `Podjąłeś(ęłaś) już decyzję dotyczącą wolontariuszy którzy wzięli udział w wydarzeniu.<br/> Możesz ją zmienić do: <b>${institutionPresenceState?.decisionChangeDeadlineDate.toLocaleDateString()}</b>`;
    }
    if (loggedUser.hasUserRole(UserRoleEnum.Volunteer)) {
      return `Status twojej decyzji dotyczącej wzięcia udziału w wolontariacie: <b>${
        volunteerPresenceState?.confirmationState
      }.</b><br/> Możesz ją zmienić do: ${volunteerPresenceState?.decisionChangeDeadlineDate.toLocaleDateString()}`;
    }
    return null;
  }

  ngOnDestroy(): void {
    this._isOfferPresenceReady = undefined;
  }

  private hasVolunteerConfirmedPresence(
    canChangeDecision: boolean,
    presenceState?: VolunteerPresenceModel
  ): boolean {
    return (
      (presenceState?.confirmationState === PresenceStateEnum.Confirmed ||
        presenceState?.confirmationState === PresenceStateEnum.Denied) &&
      canChangeDecision
    );
  }
}
