import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AdvertisementBenefit } from 'src/app/core/model/advertisement.model';
import { FormGroup, Validators } from '@angular/forms';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-advertisement-optional-info',
  templateUrl: './advertisement-optional-info.component.html',
  styleUrls: ['./advertisement-optional-info.component.scss'],
})
export class AdvertisementOptionalInfoComponent implements OnInit {
  @Input() benefits: AdvertisementBenefit[] = [];
  @Input() optionalInfoFormGroup = new FormGroup<any>({});
  @Input() canSubmitForm: boolean = false;
  @Output() formSubmitEvent = new EventEmitter<any>();

  constructor() {}

  ngOnInit(): void {}

  public onIsPoznanOnlyChange(toggleChange: MatSlideToggleChange) {
    if (toggleChange.checked) {
      this.optionalInfoFormGroup.controls['eventPlace'].disable({
        onlySelf: true,
      });
      this.optionalInfoFormGroup.controls['eventPlace'].removeValidators([
        Validators.required,
      ]);
    } else {
      this.optionalInfoFormGroup.controls['eventPlace'].enable({
        onlySelf: true,
      });
      this.optionalInfoFormGroup.controls['eventPlace'].addValidators([
        Validators.required,
      ]);
    }
  }
}
