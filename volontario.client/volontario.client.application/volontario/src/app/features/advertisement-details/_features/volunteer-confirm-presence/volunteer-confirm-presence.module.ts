import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { VolunteerConfirmPresenceComponent } from './volunteer-confirm-presence.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [VolunteerConfirmPresenceComponent],
  imports: [
    CommonModule,
    MatDialogModule,
    NgOptimizedImage,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
  ],
  exports: [VolunteerConfirmPresenceComponent],
})
export class VolunteerConfirmPresenceModule {}
