import { Component, OnInit } from '@angular/core';
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';

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
  constructor() {}

  ngOnInit(): void {}

  public toggleMenu(): void {
    this.showSidebar = !this.showSidebar;
  }
}
