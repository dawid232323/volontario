import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EvaluationCardComponent } from './evaluation-card.component';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MatListModule } from '@angular/material/list';

@NgModule({
  declarations: [EvaluationCardComponent],
  exports: [EvaluationCardComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    RouterLink,
    MatListModule,
  ],
})
export class EvaluationCardModule {}
