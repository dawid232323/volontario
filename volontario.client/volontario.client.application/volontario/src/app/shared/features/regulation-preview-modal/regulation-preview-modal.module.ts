import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegulationPreviewModalComponent } from './regulation-preview-modal.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [RegulationPreviewModalComponent],
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  exports: [RegulationPreviewModalComponent],
})
export class RegulationPreviewModalModule {}
