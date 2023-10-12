import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DefaultAngularEditorConfigProvider } from 'src/app/utils/angular-editor.const';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SingleUserDetailsConfigProviderIf } from 'src/app/features/user-details/_features/basic-user-details/_features/single-field-user-details-form/single-user-details-config.provider';

@Component({
  selector: 'app-single-field-user-details-form',
  templateUrl: './single-field-user-details-form.component.html',
  styleUrls: ['./single-field-user-details-form.component.scss'],
})
export class SingleFieldUserDetailsFormComponent {
  private readonly _dialogTitle?: string;

  public userDetailsForm!: FormGroup;
  public editorConfig: AngularEditorConfig;
  constructor(
    private formBuilder: FormBuilder,
    private matDialogRef: MatDialogRef<SingleFieldUserDetailsFormComponent>,
    @Inject(MAT_DIALOG_DATA)
    private initialData: SingleUserDetailsConfigProviderIf
  ) {
    this.editorConfig = new DefaultAngularEditorConfigProvider().config;
    this._dialogTitle = initialData.title;
    this.buildForm(initialData.initialData);
  }

  public get dialogTitle(): string | undefined {
    return this._dialogTitle;
  }

  public onFormSubmit() {
    if (this.userDetailsForm.invalid) {
      return;
    }
    this.matDialogRef.close(this.userDetailsForm.value['detailsField']);
  }

  private buildForm(initialValue?: string) {
    this.userDetailsForm = this.formBuilder.group({
      detailsField: [
        initialValue || null,
        [Validators.required, Validators.maxLength(2800)],
      ],
    });
  }
}
