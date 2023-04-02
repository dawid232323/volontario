import { Component, OnInit } from '@angular/core';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/core/service/user.service';

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

  ngOnInit(): void {
    this.userService.getCurrentUserData().subscribe({
      next: result => console.log(result),
      error: err => console.log(err),
    });
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['login']);
  }
}
