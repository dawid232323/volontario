import { Component, OnDestroy, OnInit } from '@angular/core';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { Subscription } from 'rxjs';
import { AdvertisementService } from 'src/app/core/service/advertisement.service';
import { isNil } from 'lodash';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss', '../../styles/material-styles.scss'],
})
export class NavComponent implements OnInit, OnDestroy {
  constructor(
    public authService: SecurityService,
    private router: Router,
    private userService: UserService,
    private offerService: AdvertisementService
  ) {}

  public loggedUser?: User;
  private subscriptions = new Subscription();

  ngOnInit(): void {
    this.subscriptions.add(
      this.authService.loginEvent.subscribe(this.downloadData.bind(this))
    );
    this.downloadData();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  private downloadData() {
    this.userService.getCurrentUserData().subscribe(result => {
      this.loggedUser = result;
    });
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['login']);
    this.loggedUser = undefined;
  }

  onReportIssue(): void {
    this.router.navigate(['report-issue']);
  }

  routeToAddAdvertisement() {
    if (this.router.url === '/advertisement/add') {
      this.offerService.addAdvertisementReloadEvent.next();
      return;
    }
    this.router.navigate(['advertisement', 'add']);
  }

  public routeToAdminUsers() {
    this.router.navigate(['admin', 'users']);
  }

  public routeToManageDictValues() {
    this.router.navigate(['moderator', 'manage-dict-values']);
  }

  public onShowProfileCLick() {
    if (isNil(this.loggedUser)) {
      return;
    }
    this.router.navigate(['user', this.loggedUser.id]);
  }

  protected readonly UserRoleEnum = UserRoleEnum;
}
