import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AdvertisementService } from 'src/app/core/service/advertisement.service';
import {
  InstitutionVoluntaryPresenceModel,
  VolunteerPresenceDto,
} from 'src/app/core/model/offer-presence.model';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';
import { ConfirmVolunteerPresenceIf } from 'src/app/features/institution-offer-presence/_features/institution-offer-presence-volunteers-list/institution-offer-presence-volunteers-list.component';

@Component({
  selector: 'app-institution-offer-presence',
  templateUrl: './institution-offer-presence.component.html',
  styleUrls: ['./institution-offer-presence.component.scss'],
})
export class InstitutionOfferPresenceComponent implements OnInit {
  public isLoadingData = true;

  private _availableVolunteers: ConfirmVolunteerPresenceIf[] = [];
  private _presenceState?: InstitutionVoluntaryPresenceModel;
  private readonly _offerId: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private offerService: AdvertisementService,
    private errorDialogService: ErrorDialogService,
    private router: Router
  ) {
    this._offerId = +this.activatedRoute.snapshot.queryParams['o'];
  }

  ngOnInit(): void {
    this.offerService
      .getInstitutionVoluntaryPresenceState(this._offerId)
      .subscribe(presenceState => {
        if (presenceState === false) {
          this.handlePresenceError();
          return;
        }
        this.handlePresenceSuccess(presenceState);
      });
  }

  public onPresenceConfirmation() {
    this.isLoadingData = true;
    const volunteerPresences = this.getMappedPresenceDtos();
    this.offerService
      .determineVoluntaryPresenceForInstitution(
        this._offerId,
        volunteerPresences
      )
      .subscribe({
        next: this.onReturn.bind(this),
        error: () => (this.isLoadingData = false),
      });
  }

  public onPostpone() {
    this.offerService
      .postponePresenceConfirmationInstitution(this._offerId)
      .subscribe(this.onReturn.bind(this));
  }

  public onReturn() {
    this.router.navigate(['advertisement', this._offerId]);
  }

  private proceedFirstDecision() {
    this.offerService
      .getConfirmableVolunteersForOffer(this._offerId)
      .subscribe(volunteers => {
        this._availableVolunteers = volunteers.map(volunteer => {
          return {
            volunteerId: volunteer.id,
            fullName: volunteer.firstName.concat(' ', volunteer.lastName),
            hasTakenPart: true,
          };
        });
        this.isLoadingData = false;
      });
  }

  public get availableVolunteers(): ConfirmVolunteerPresenceIf[] {
    return this._availableVolunteers;
  }

  public get canPostponeReminder(): boolean {
    return this._presenceState?.canPostponeReminder || false;
  }

  private proceedDecisionChange(
    decisionDetails: InstitutionVoluntaryPresenceModel
  ) {
    if (!decisionDetails.canDecisionBeChanged) {
      throw new Error(
        `Termin na zmianę decyzji upłynął ${decisionDetails.decisionChangeDeadlineDate.toLocaleDateString()}`
      );
    }
    this.proceedFirstDecision();
  }

  private getMappedPresenceDtos(): VolunteerPresenceDto[] {
    return this._availableVolunteers.map(volunteer => {
      return new VolunteerPresenceDto(
        volunteer.volunteerId,
        volunteer.hasTakenPart ? 'CONFIRMED' : 'DENIED'
      );
    });
  }

  private handlePresenceSuccess(
    presenceState: InstitutionVoluntaryPresenceModel
  ) {
    this._presenceState = presenceState;
    if (!presenceState.wasPresenceConfirmed) {
      this.proceedFirstDecision();
    } else {
      this.proceedDecisionChange(presenceState);
    }
  }

  private handlePresenceError() {
    const dialogRef = this.errorDialogService.openDefaultErrorDialog({
      error: new Error(
        'Ogłoszenie nie ma aplikacji zatwierdzonych do dalszej rekrutacji'
      ),
      dialogTitle: 'Nie można załadować listy wolontariuszy',
      dialogMessage:
        'Aplikacja żadnego z wolontariuszy nie została zaakceptowana',
    });
    dialogRef.afterClosed().subscribe(this.onReturn.bind(this));
  }
}
