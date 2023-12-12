import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RegulationType } from 'src/app/core/model/configuration.model';

export interface RegulationPreviewModalInitialData {
  regulationType: RegulationType;
  modalContent: string;
}

@Component({
  selector: 'app-regulation-preview-modal',
  templateUrl: './regulation-preview-modal.component.html',
  styleUrls: ['./regulation-preview-modal.component.scss'],
})
export class RegulationPreviewModalComponent implements OnInit {
  private readonly _title: string;
  private readonly _content: string;

  constructor(
    private dialogRef: MatDialogRef<RegulationPreviewModalComponent>,
    @Inject(MAT_DIALOG_DATA)
    private initialData: RegulationPreviewModalInitialData
  ) {
    this._title = this.determineModalTitle(initialData.regulationType);
    this._content = initialData.modalContent;
  }

  ngOnInit(): void {}

  private determineModalTitle(regulationType: RegulationType): string {
    switch (regulationType) {
      case RegulationType.Use:
        return 'Regulamin Volontario';
      case RegulationType.Rodo:
        return 'Regulamin RODO';
      default:
        return '';
    }
  }

  get title(): string {
    return this._title;
  }

  get content(): string {
    return this._content;
  }
}
