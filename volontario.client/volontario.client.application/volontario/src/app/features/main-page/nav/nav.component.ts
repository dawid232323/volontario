import { Component, Input, OnInit } from '@angular/core';
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { LandingPageSection } from 'src/app/core/model/configuration.model';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss'],
  animations: [
    trigger('sidebarTrigger', [
      transition(':enter', [
        style({ transform: 'translateX(-100%)' }),
        animate('300ms ease-in', style({ transform: 'translateY(0%)' })),
      ]),
      state('open', style({ transform: 'translateX(0%)' })),
      state('close', style({ transform: 'translateX(-100%)' })),
      transition('open => close', [animate('300ms ease-in')]),
      transition('close => open', [animate('300ms ease-out')]),
    ]),
  ],
})
export class MainPageNavComponent implements OnInit {
  public showSidebar: boolean = false;

  @Input() sections: LandingPageSection[] = [];
  @Input() isPreviewMode: boolean = false;

  constructor(private authService: SecurityService, private router: Router) {}

  ngOnInit(): void {}

  public toggleMenu(): void {
    this.showSidebar = !this.showSidebar;
  }

  public get isUserLoggedIn(): boolean {
    return this.authService.isUserLoggedIn();
  }

  onBackToEdit() {
    this.router.navigate(['admin', 'edit-main-page'], {
      queryParams: { prev: this.isPreviewMode },
    });
  }
}
