import { Component, OnInit } from '@angular/core';
import { InstitutionService } from 'src/app/core/service/institution.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Institution } from 'src/app/core/model/institution.model';
import {
  AdvertisementFilterIf,
  AdvertisementService,
  AdvertisementVisibilityEnum,
} from 'src/app/core/service/advertisement.service';
import { forkJoin } from 'rxjs';
import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';

@Component({
  selector: 'app-institution-details',
  templateUrl: './institution-details.component.html',
  styleUrls: ['./institution-details.component.scss'],
})
export class InstitutionDetailsComponent implements OnInit {
  private _institutionId = 0;
  private _loadedInstitution?: Institution;
  private _offers: AdvertisementPreview[] = [];
  private _canManageInstitution = false;
  private _loggedUser?: User;

  constructor(
    private institutionService: InstitutionService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private advertisementService: AdvertisementService,
    private userService: UserService
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

  private setInitialData() {
    this._institutionId =
      +this.activatedRoute.snapshot.params['institution_id'];
    const offerQueryParams = this.getQueryParams();
    forkJoin([
      this.institutionService.getInstitutionDetails(this._institutionId),
      this.advertisementService.getAdvertisementPreviews(offerQueryParams),
      this.userService.getCurrentUserData(),
    ]).subscribe(([institutionData, offers, userData]) => {
      this._loadedInstitution = institutionData;
      this._offers = offers.content;
      this._loggedUser = userData;
      this._canManageInstitution = this.institutionService.canManageInstitution(
        this._loggedUser!,
        this._loadedInstitution!
      );
    });
  }

  private getQueryParams(): AdvertisementFilterIf {
    return {
      institutionId: this._institutionId,
      visibility: AdvertisementVisibilityEnum.Active,
    };
  }
}
