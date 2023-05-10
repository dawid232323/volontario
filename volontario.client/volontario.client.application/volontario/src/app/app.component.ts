import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { SecurityService } from './core/service/security/security.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'volontario';
  public showNav: boolean = true;
  constructor(router: Router, public authService: SecurityService) {
    router.events.subscribe(pathName => {
      if (pathName instanceof NavigationEnd && !authService.isUserLoggedIn()) {
        this.showNav = false;
      }
    });
  }
}
