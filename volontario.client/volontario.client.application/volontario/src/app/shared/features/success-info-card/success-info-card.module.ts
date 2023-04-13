import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { SuccessInfoCardComponent } from 'src/app/shared/features/success-info-card/success-info-card.component';

@NgModule({
  declarations: [SuccessInfoCardComponent],
  imports: [CommonModule, MatButtonModule, MatCardModule, MatIconModule],
  exports: [SuccessInfoCardComponent],
})
export class SuccessInfoCardModule {}
