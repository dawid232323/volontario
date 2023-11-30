import { Component, Inject, OnInit } from '@angular/core';
import { cloneDeep, isNil } from 'lodash';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { hasMaxLengthError } from 'src/app/utils/validator.utils';
import { EvaluationAvailableOffer } from 'src/app/core/model/evaluation.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface EvaluationModalData {
  availableOffers?: EvaluationAvailableOffer[];
  evaluationValue?: number;
  selectedOfferId?: number;
  comment?: string;
  evaluationMaxScale?: number;
}

@Component({
  selector: 'app-evaluation-modal',
  templateUrl: './evaluation-modal.component.html',
  styleUrls: ['./evaluation-modal.component.scss'],
})
export class EvaluationModalComponent implements OnInit {
  public form!: FormGroup;

  private _evaluationMaxScale = 5;
  private _evaluationSum = 2;
  private _evaluationSumBeforeHover = 0;
  private _releaseLock = false;
  private _availableOffers: EvaluationAvailableOffer[] = [];

  constructor(
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) private initialData: EvaluationModalData,
    private dialogRef: MatDialogRef<EvaluationModalComponent>
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.setInitialData();
  }

  public onFormSubmit() {
    if (this.form.invalid) {
      return;
    }
    const resultData = this.getResultDataFromForm();
    this.dialogRef.close(resultData);
  }

  public onFormDismiss() {
    this.dialogRef.close(null);
  }

  public isStarHighlighted(starIndex: number): boolean {
    return starIndex + 1 <= this._evaluationSum;
  }

  public onStarClicked(starIndex: number): void {
    this._evaluationSum = starIndex + 1;
    this._evaluationSumBeforeHover = starIndex + 1;
    this.form.controls['evaluation']?.patchValue(starIndex + 1);
  }

  public onMouseHoverEnter(starIndex: number) {
    if (this._releaseLock) {
      return;
    }
    this._releaseLock = true;
    this._evaluationSumBeforeHover = cloneDeep(this._evaluationSum);
    this._evaluationSum = starIndex + 1;
  }

  public onMouseHoverLeave() {
    this._evaluationSum = cloneDeep(this._evaluationSumBeforeHover);
    this._releaseLock = false;
  }

  private createForm() {
    this.form = this.formBuilder.group({
      offerId: [null, [Validators.required]],
      evaluation: [
        this._evaluationSum,
        [Validators.required, Validators.min(1)],
      ],
      evaluationDescription: [null, [Validators.maxLength(500)]],
    });
  }

  private setInitialData() {
    this._availableOffers = this.initialData?.availableOffers || [];
    if (isNil(this.initialData)) {
      return;
    }
    if (!isNil(this.initialData.evaluationMaxScale)) {
      this._evaluationMaxScale = this.initialData.evaluationMaxScale;
    }
    if (!isNil(this.initialData.evaluationValue)) {
      this._evaluationSum = this.initialData.evaluationValue;
      this.form.controls['evaluation'].patchValue(
        this.initialData.evaluationValue
      );
    }
    if (!isNil(this.initialData.comment)) {
      this.form.controls['evaluationDescription'].patchValue(
        this.initialData.comment
      );
    }
    if (!isNil(this.initialData.selectedOfferId)) {
      this.form.controls['offerId'].patchValue(
        this.initialData.selectedOfferId
      );
    }
  }

  private getResultDataFromForm(): EvaluationModalData {
    return {
      selectedOfferId: this.form.value['offerId'],
      evaluationValue: this.form.value['evaluation'],
      comment: this.form.value['evaluationDescription'],
    };
  }

  public get evaluationMaxScale(): number {
    return this._evaluationMaxScale;
  }

  public get availableOffers(): EvaluationAvailableOffer[] {
    return this._availableOffers;
  }

  protected readonly hasMaxLengthError = hasMaxLengthError;
}
