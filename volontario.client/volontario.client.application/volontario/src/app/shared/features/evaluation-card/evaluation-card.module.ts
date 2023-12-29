import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EvaluationCardComponent } from './evaluation-card.component';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  declarations: [EvaluationCardComponent],
  exports: [EvaluationCardComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    RouterLink,
    MatListModule,
    MatTooltipModule,
    MatMenuModule,
    MatButtonModule,
    MatFormFieldModule,
  ],
})
export class EvaluationCardModule {}
