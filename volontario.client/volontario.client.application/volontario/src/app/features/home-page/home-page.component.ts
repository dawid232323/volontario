import { Component, OnInit } from '@angular/core';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/core/service/user.service';
import { User } from 'src/app/core/model/user.model';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {
  constructor(
    private authService: SecurityService,
    private router: Router,
    private userService: UserService
  ) {}

  public loggedUser?: User;

  ngOnInit(): void {
    this.userService.getCurrentUserData().subscribe({
      next: result => (this.loggedUser = result),
      error: err => console.log(err),
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
