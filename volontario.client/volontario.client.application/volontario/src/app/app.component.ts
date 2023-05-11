import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { SecurityService } from './core/service/security/security.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'volontario';
  public showNav: boolean = true;
  private subscriptions = new Subscription();
  constructor(private router: Router, private authService: SecurityService) {}

  ngOnInit() {
    this.subscriptions.add(
      this.authService.loginEvent.subscribe(() => {
        this.showNav = true;
      })
    );
    this.router.events.subscribe(pathName => {
      if (
        pathName instanceof NavigationEnd &&
        !this.authService.isUserLoggedIn()
      ) {
        this.showNav = false;
      }
    });
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
