import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PasswordCheckCardComponent } from 'src/app/shared/features/password-check-card/password-check-card.component';

@NgModule({
  declarations: [PasswordCheckCardComponent],
  imports: [CommonModule],
  exports: [PasswordCheckCardComponent],
})
export class PasswordCheckCardModule {}
