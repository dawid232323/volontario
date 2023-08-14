import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavComponent } from './nav.component';
import { LogoModule } from '../logo/logo.module';
import { RouterLink, RouterLinkWithHref } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';

@NgModule({
  declarations: [NavComponent],
  imports: [
    CommonModule,
    LogoModule,
    RouterLinkWithHref,
    MatButtonModule,
    MatMenuModule,
    RouterLink,
  ],
  exports: [NavComponent],
})
export class NavModule {}
