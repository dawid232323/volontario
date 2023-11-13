import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InstitutionOfferPresenceVolunteersListComponent } from './institution-offer-presence-volunteers-list.component';
import { MatTableModule } from '@angular/material/table';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [InstitutionOfferPresenceVolunteersListComponent],
  imports: [CommonModule, MatTableModule, MatSlideToggleModule, FormsModule],
  exports: [InstitutionOfferPresenceVolunteersListComponent],
})
export class InstitutionOfferPresenceVolunteersListModule {}
