import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { SecurityService } from './core/service/security/security.service';
import { Subscription } from 'rxjs';
import { noNavUrls } from 'src/app/utils/url.util';

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
      if (!(pathName instanceof NavigationStart)) {
        return;
      }
      if (
        this.hasNoNavUrl(pathName.url) ||
        !this.authService.isUserLoggedIn()
      ) {
        this.showNav = false;
        return;
      }
      this.showNav = true;
    });
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  private hasNoNavUrl(url: string) {
    const withoutQueryParams = url.split('?')[0];
    const withoutPageRef = withoutQueryParams.split('#')[0];
    return noNavUrls.has(withoutPageRef);
  }
}
