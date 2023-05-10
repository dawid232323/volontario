import { Component, OnInit } from '@angular/core';
import { SecurityService } from '../../../core/service/security/security.service';
import { Router } from '@angular/router';
import { UserService } from '../../../core/service/user.service';
import { User } from '../../../core/model/user.model';
import { UserRoleEnum } from '../../../core/model/user-role.model';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss'],
})
export class NavComponent implements OnInit {
  constructor(
    public authService: SecurityService,
    private router: Router,
    private userService: UserService
  ) {}

  public loggedUser?: User;

  ngOnInit(): void {
    this.userService.getCurrentUserData().subscribe(result => {
      this.loggedUser = result;
    });
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['login']);
    this.loggedUser = undefined;
  }

  routeToAddAdvertisement() {
    this.router.navigate(['advertisement/add']);
  }

  protected readonly UserRoleEnum = UserRoleEnum;
}
