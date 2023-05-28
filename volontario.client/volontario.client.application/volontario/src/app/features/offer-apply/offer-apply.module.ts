import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfferApplyComponent } from 'src/app/features/offer-apply/offer-apply.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatStepperModule } from '@angular/material/stepper';
import { ContactInfoFormModule } from 'src/app/features/offer-apply/_features/contact-info-form/contact-info-form.module';
import { ReasonFormModule } from 'src/app/features/offer-apply/_features/reason-form/reason-form.module';
import { SuccessInfoCardModule } from 'src/app/shared/features/success-info-card/success-info-card.module';

@NgModule({
  declarations: [OfferApplyComponent],
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatStepperModule,
    ContactInfoFormModule,
    ReasonFormModule,
    SuccessInfoCardModule,
  ],
  exports: [OfferApplyComponent],
})
export class OfferApplyModule {}
