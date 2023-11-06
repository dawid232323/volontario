import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { InfoCardComponent } from 'src/app/shared/features/success-info-card/info-card.component';

@NgModule({
  declarations: [InfoCardComponent],
  imports: [CommonModule, MatButtonModule, MatCardModule, MatIconModule],
  exports: [InfoCardComponent],
})
export class InfoCardModule {}
