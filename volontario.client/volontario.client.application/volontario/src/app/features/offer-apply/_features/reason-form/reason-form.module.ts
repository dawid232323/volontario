import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReasonFormComponent } from 'src/app/features/offer-apply/_features/reason-form/reason-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { RouterLink } from '@angular/router';

@NgModule({
  declarations: [ReasonFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatStepperModule,
    RouterLink,
  ],
  exports: [ReasonFormComponent],
})
export class ReasonFormModule {}
