import { Component, OnInit } from '@angular/core';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import {
  AdvertisementFilterIf,
  AdvertisementService,
} from '../../core/service/advertisement.service';
import { AdvertisementPreview } from '../../core/model/advertisement.model';
import { forkJoin, Observable } from 'rxjs';
import { PageableModelInterface } from 'src/app/core/model/pageable.model';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {
  public currentlySelectedPageIndex: number = 0;
  public currentlySelectedPageSize: number = 5;
  public isLoadingData: boolean = false;
  public selectedAdvertisements: AdvertisementPreview[] = [];
  public allAdvertisements: AdvertisementPreview[] = [];
  private _filterData?: AdvertisementFilterIf = {};
  private _institutionAdditionalFilterData?: AdvertisementFilterIf;
  private _volunteerFilterData?: AdvertisementFilterIf;

  constructor(
    private authService: SecurityService,
    private router: Router,
    private userService: UserService,
    private advertisementService: AdvertisementService
  ) {}

  public loggedUser?: User;

  ngOnInit() {
    this.userService.getCurrentUserData().subscribe({
      next: result => {
        this.loggedUser = result;
        this.handleFilterAssignmentsAndDataDownload();
      },
      error: err => {
        throw new Error(err.error);
      },
    });
  }

  private handleFilterAssignmentsAndDataDownload() {
    if (this.loggedUser?.hasUserRole(UserRoleEnum.Volunteer)) {
      this.handleFiltersForVolunteer();
      this.getVolunteerAdvertisements();
      return;
    }
    if (
      this.loggedUser?.hasUserRoles([
        UserRoleEnum.InstitutionWorker,
        UserRoleEnum.InstitutionAdmin,
      ])
    ) {
      this.handleFiltersForInstitution();
      this.getInstitutionAdvertisements();
      return;
    }
    this.getModeratorAdvertisements();
  }

  private handleFiltersForVolunteer() {
    this._volunteerFilterData = {
      interestCategoryIds:
        this.loggedUser?.volunteerData?.interestCategories.map(
          category => category.id
        ),
    };
  }

  private handleFiltersForInstitution() {
    this.isLoadingData = true;
    this._filterData = {
      institutionId: this.loggedUser!.institution!.id!,
      contactPersonId: this.loggedUser?.id,
    };
    this._institutionAdditionalFilterData = {
      institutionId: this.loggedUser!.institution!.id!,
    };
  }

  private getVolunteerAdvertisements() {
    this.isLoadingData = true;
    this.getAdvertisements(this._volunteerFilterData!).subscribe(previews => {
      this.selectedAdvertisements = previews.content;
      this.isLoadingData = false;
    });
  }

  private getInstitutionAdvertisements() {
    forkJoin([
      this.getAdvertisements(this._filterData!),
      this.getAdvertisements(this._institutionAdditionalFilterData!),
    ]).subscribe(([selectedAdvertisements, allAdvertisements]) => {
      this.selectedAdvertisements = selectedAdvertisements.content;
      this.allAdvertisements = allAdvertisements.content;
      this.isLoadingData = false;
    });
  }

  private getModeratorAdvertisements() {
    this.isLoadingData = true;
    this.getAdvertisements({}).subscribe(previews => {
      this.selectedAdvertisements = previews.content;
      this.isLoadingData = false;
    });
  }

  private getAdvertisements(
    filter: AdvertisementFilterIf
  ): Observable<PageableModelInterface<AdvertisementPreview>> {
    return this.advertisementService.getAdvertisementPreviews(
      filter,
      this.currentlySelectedPageIndex,
      this.currentlySelectedPageSize
    );
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['login']);
  }

  routeToAddAdvertisement() {
    this.router.navigate(['advertisement/add']);
  }

  protected readonly UserRoleEnum = UserRoleEnum;
}
