import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RegisterInstitutionEmployeeDto } from 'src/app/core/model/institution.model';
import { hasMaxLengthError } from 'src/app/utils/validator.utils';
import { isNil } from 'lodash';

@Component({
  selector: 'app-add-employee-form',
  templateUrl: './add-employee-form.component.html',
  styleUrls: ['./add-employee-form.component.scss'],
})
export class AddEmployeeFormComponent {
  public addEmployeeForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<AddEmployeeFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { institutionId: number }
  ) {
    this.addEmployeeForm = this.formBuilder.group({
      institutionId: [data.institutionId],
      firstName: [null, [Validators.required, Validators.maxLength(100)]],
      lastName: [null, [Validators.required, Validators.maxLength(100)]],
      phoneNumber: [null, [Validators.required, Validators.minLength(9), Validators.maxLength(9)]],
      contactEmail: [null, [Validators.required, Validators.email]],
    });
  }

  public onFormClose() {
    const dto = RegisterInstitutionEmployeeDto.fromPayload(this.addEmployeeForm.value);
    this.dialogRef.close(dto);
  }

  protected readonly hasMaxLengthError = hasMaxLengthError;
  protected readonly isNil = isNil;
}
