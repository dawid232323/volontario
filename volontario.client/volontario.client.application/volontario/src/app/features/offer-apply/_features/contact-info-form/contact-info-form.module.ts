import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactInfoFormComponent } from 'src/app/features/offer-apply/_features/contact-info-form/contact-info-form.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { RouterLink } from '@angular/router';

@NgModule({
  declarations: [ContactInfoFormComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatIconModule,
    FormsModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatStepperModule,
    RouterLink,
  ],
  exports: [ContactInfoFormComponent],
})
export class ContactInfoFormModule {}
