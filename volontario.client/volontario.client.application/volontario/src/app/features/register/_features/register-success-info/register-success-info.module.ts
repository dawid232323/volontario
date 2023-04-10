import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterSuccessInfoComponent } from 'src/app/features/register/_features/register-success-info/register-success-info.component';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [RegisterSuccessInfoComponent],
  imports: [CommonModule, MatCardModule, MatIconModule, MatButtonModule],
  exports: [RegisterSuccessInfoComponent],
})
export class RegisterSuccessInfoModule {}
