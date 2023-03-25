import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SideBannerComponent } from './side-banner.component';
import { LogoModule } from '../logo/logo.module';

@NgModule({
  declarations: [SideBannerComponent],
  imports: [CommonModule, LogoModule],
  exports: [SideBannerComponent],
})
export class SideBannerModule {}
