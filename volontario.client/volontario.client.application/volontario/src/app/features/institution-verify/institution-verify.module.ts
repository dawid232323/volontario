import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionVerifyComponent } from 'src/app/features/institution-verify/institution-verify.component';
import { LogoModule } from 'src/app/shared/features/logo/logo.module';
import { SideBannerModule } from 'src/app/shared/features/side-banner/side-banner.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SuccessInfoCardModule } from 'src/app/shared/features/success-info-card/success-info-card.module';

@NgModule({
  declarations: [InstitutionVerifyComponent],
  imports: [
    CommonModule,
    LogoModule,
    SideBannerModule,
    MatProgressSpinnerModule,
    SuccessInfoCardModule,
  ],
  exports: [InstitutionVerifyComponent],
})
export class InstitutionVerifyModule {}
