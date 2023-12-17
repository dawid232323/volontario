import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
  DictionaryValueTypeEnum,
  DictValueOperationInterface,
  DictValueOperationTypeEnum,
} from 'src/app/features/manage-dict-values/_features/dict-values-list/dict-values-list.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';
import { cloneDeep } from 'lodash';

@Component({
  selector: 'app-dict-value-form',
  templateUrl: './dict-value-form.component.html',
  styleUrls: ['./dict-value-form.component.scss'],
})
export class DictValueFormComponent implements OnInit {
  private _formTitle = 'Dodaj nową wartość';
  private _dictValueFormGroup = new FormGroup<any>({});

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<DictValueFormComponent>,
    @Inject(MAT_DIALOG_DATA) private data: DictValueOperationInterface
  ) {}

  ngOnInit(): void {
    this.prepareForm();
    this.setInitialValues();
  }

  public get formTitle(): string {
    return this._formTitle;
  }

  public get dictValueFormGroup(): FormGroup<any> {
    return this._dictValueFormGroup;
  }

  public get shouldShowDescription(): boolean {
    return this.data.valueType !== DictionaryValueTypeEnum.AddBenefits;
  }

  public onConfirmButtonClicked() {
    if (this._dictValueFormGroup.invalid) {
      return;
    }
    const result: DictionaryValueInterface = {
      ...this.data.operationData!,
      name: this.dictValueFormGroup.value['name'],
      description: this.dictValueFormGroup.value['description'],
    };
    this.dialogRef?.close(result);
  }

  private prepareForm() {
    if (this.data.operationType === DictValueOperationTypeEnum.Edit) {
      this._formTitle = 'Edytuj wartość';
    }
    this._dictValueFormGroup = this.formBuilder.group({
      name: [null, [Validators.required, Validators.maxLength(250)]],
      description: [null, [Validators.maxLength(100)]],
    });
    if (this.shouldShowDescription) {
      this._dictValueFormGroup.controls['description'].addValidators(
        Validators.required
      );
      this._dictValueFormGroup.controls['description'].updateValueAndValidity();
    }
  }

  private setInitialValues() {
    if (this.data.operationType !== DictValueOperationTypeEnum.Edit) {
      return;
    }
    this._dictValueFormGroup.controls['name'].setValue(
      this.data.operationData?.name
    );
    if (this.shouldShowDescription) {
      this._dictValueFormGroup.controls['description'].setValue(
        this.data.operationData?.description
      );
    }
  }
}
