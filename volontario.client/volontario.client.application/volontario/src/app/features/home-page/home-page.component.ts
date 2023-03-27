import { Component, OnInit } from '@angular/core';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent implements OnInit {
  constructor(private authService: SecurityService, private router: Router) {}

  ngOnInit(): void {}

  onLogout() {
    this.authService.logout();
    this.router.navigate(['login']);
  }
}
