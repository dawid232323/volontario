import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { BasicUserDetailsComponent } from './basic-user-details.component';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { SingleFieldUserDetailsFormModule } from 'src/app/features/user-details/_features/basic-user-details/_features/single-field-user-details-form/single-field-user-details-form.module';

@NgModule({
  declarations: [BasicUserDetailsComponent],
  imports: [
    CommonModule,
    MatCardModule,
    NgOptimizedImage,
    MatIconModule,
    MatListModule,
    MatTooltipModule,
    MatChipsModule,
    MatButtonModule,
    SingleFieldUserDetailsFormModule,
  ],
  exports: [BasicUserDetailsComponent],
})
export class BasicUserDetailsModule {}
