import { Component, OnInit, ViewChild } from '@angular/core';
import { InstitutionService } from 'src/app/core/service/institution.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Institution } from 'src/app/core/model/institution.model';
import {
  AdvertisementFilterIf,
  AdvertisementService,
  AdvertisementVisibilityEnum,
} from 'src/app/core/service/advertisement.service';
import { firstValueFrom, forkJoin } from 'rxjs';
import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';
import { EvaluationService } from 'src/app/core/service/evaluation.service';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import {
  EvaluationDto,
  OffersToEvaluateIf,
  UserEvaluation,
} from 'src/app/core/model/evaluation.model';
import { isNil } from 'lodash';
import { MatTabGroup } from '@angular/material/tabs';
import { updateActiveUrl } from 'src/app/utils/url.util';
import { EvaluationModalData } from 'src/app/shared/features/evaluation-modal/evaluation-modal.component';

enum SelectedTabIndex {
  basicData,
  institutionEvaluation,
}

@Component({
  selector: 'app-institution-details',
  templateUrl: './institution-details.component.html',
  styleUrls: ['./institution-details.component.scss'],
})
export class InstitutionDetailsComponent implements OnInit {
  @ViewChild(MatTabGroup) matTab!: MatTabGroup;

  private _institutionId = 0;
  private _loadedInstitution?: Institution;
  private _offers: AdvertisementPreview[] = [];
  private _canManageInstitution = false;
  private _loggedUser?: User;
  private _evaluation?: UserEvaluation;
  private _offerToEvaluate?: OffersToEvaluateIf;
  public isLoadingData: boolean = true;

  constructor(
    private institutionService: InstitutionService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private advertisementService: AdvertisementService,
    private userService: UserService,
    private evaluationService: EvaluationService
  ) {}

  public ngOnInit(): void {
    this.setInitialData();
  }

  public get loadedInstitution(): Institution | undefined {
    return this._loadedInstitution;
  }

  public get offers(): AdvertisementPreview[] {
    return this._offers;
  }

  get canManageInstitution(): boolean {
    return this._canManageInstitution;
  }

  public onEditInstitutionData() {
    if (!this.canManageInstitution) {
      return;
    }
    this.router.navigate(['institution', 'edit', this._institutionId]);
  }

  public onManageWorkers() {
    if (!this.canManageInstitution) {
      return;
    }
    this.router.navigate(['institution', 'workers', this._institutionId]);
  }

  public onSelectTabIndexChange($event: number) {
    updateActiveUrl(this.router, this.activatedRoute, { tab: $event }, 'merge');
  }

  public onEvaluationPerformed(evalData: EvaluationModalData) {
    this.isLoadingData = true;
    const body = this.resolveRequestDto(evalData);
    this.evaluationService
      .rateInstitution(this._institutionId, body)
      .subscribe(async () => {
        await this.onAfterRating();
        this.isLoadingData = false;
      });
  }

  private setInitialData() {
    this.isLoadingData = true;
    this._institutionId =
      +this.activatedRoute.snapshot.params['institution_id'];
    const offerQueryParams = this.getQueryParams();
    forkJoin([
      this.institutionService.getInstitutionDetails(this._institutionId),
      this.advertisementService.getAdvertisementPreviews(offerQueryParams),
      this.userService.getCurrentUserData(),
      this.evaluationService.getInstitutionEvaluation(this._institutionId),
    ]).subscribe(
      async ([institutionData, offers, userData, institutionEvaluation]) => {
        this._loadedInstitution = institutionData;
        this._offers = offers.content;
        this._loggedUser = userData;
        this._canManageInstitution =
          this.institutionService.canManageInstitution(
            this._loggedUser!,
            this._loadedInstitution!
          );
        this._evaluation = institutionEvaluation;
        if (this._loggedUser?.hasUserRole(UserRoleEnum.Volunteer)) {
          this._offerToEvaluate = await firstValueFrom(
            this.evaluationService.getVolunteerOffersToRateInstitution(
              this._loggedUser!.id
            )
          );
        }
        this.determineSelectedTabIndex();
        this.isLoadingData = false;
      },
      () => this.router.navigate(['home'])
    );
  }

  private async onAfterRating() {
    const result = await firstValueFrom(
      forkJoin([
        this.evaluationService.getInstitutionEvaluation(this._institutionId),
        this.evaluationService.getVolunteerOffersToRateInstitution(
          this._loggedUser!.id
        ),
      ])
    );
    this._evaluation = result[0];
    this._offerToEvaluate = result[1];
  }

  private getQueryParams(): AdvertisementFilterIf {
    return {
      institutionId: this._institutionId,
      visibility: AdvertisementVisibilityEnum.Active,
    };
  }

  private determineSelectedTabIndex() {
    const queryParamTabIndex = +this.activatedRoute.snapshot.queryParams['tab'];
    if (isNil(queryParamTabIndex) || isNaN(queryParamTabIndex)) {
      return;
    }
    if (Object.values(SelectedTabIndex).indexOf(queryParamTabIndex) === -1) {
      return;
    }
    this.matTab.selectedIndex = queryParamTabIndex;
  }

  private resolveRequestDto(evalData: EvaluationModalData): EvaluationDto {
    return {
      offerId: evalData.selectedOfferId!,
      volunteerId: this._loggedUser!.id,
      rating: evalData.evaluationValue!,
      ratingReason: evalData.comment!,
    };
  }

  public get evaluation(): UserEvaluation | undefined {
    return this._evaluation;
  }

  public get offersToEvaluate(): OffersToEvaluateIf | undefined {
    return this._offerToEvaluate;
  }
}
