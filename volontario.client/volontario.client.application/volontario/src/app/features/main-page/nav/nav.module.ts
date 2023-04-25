import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainPageNavComponent } from './nav.component';
import { LogoModule } from '../../../shared/features/logo/logo.module';
import { RouterLinkWithHref } from '@angular/router';

@NgModule({
  declarations: [MainPageNavComponent],
  imports: [CommonModule, LogoModule, RouterLinkWithHref],
  exports: [MainPageNavComponent],
})
export class MainPageNavModule {}
