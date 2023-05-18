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

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {
  public currentlySelectedPageIndex: number = 0;
  public currentlySelectedPageSize: number = 5;
  public isLoadingData: boolean = false;
  public advertisements: AdvertisementPreview[] = [];
  private _filterData?: AdvertisementFilterIf;

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
        this._filterData = {
          institutionId: this.loggedUser!.institution!.id!,
          contactPersonId: this.loggedUser?.id,
        };
        this.getAdvertisements();
      },
      error: err => console.log(err),
    });
  }

  getAdvertisements() {
    this.isLoadingData = true;
    this.advertisementService
      .getAdvertisementPreviews(
        this._filterData!,
        this.currentlySelectedPageIndex,
        this.currentlySelectedPageSize
      )
      .subscribe(previews => {
        this.advertisements = previews.content;
        this.isLoadingData = false;
      });
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
