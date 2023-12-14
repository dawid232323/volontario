import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { AdvertisementBenefit } from 'src/app/core/model/advertisement.model';
import { FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AdvertisementCrudOperationType } from 'src/app/features/add-edit-advertisement/add-edit-advertisement.component';
import { isNil } from 'lodash';
import { countCharacters } from 'src/app/utils/validator.utils';

@Component({
  selector: 'app-advertisement-optional-info',
  templateUrl: './advertisement-optional-info.component.html',
  styleUrls: ['./advertisement-optional-info.component.scss'],
})
export class AdvertisementOptionalInfoComponent implements OnInit, OnDestroy {
  @Input() benefits: AdvertisementBenefit[] = [];
  @Input() optionalInfoFormGroup = new FormGroup<any>({});
  @Input() canSubmitForm: boolean = false;
  @Input() operationType = AdvertisementCrudOperationType.Add;
  @Output() formSubmitEvent = new EventEmitter<any>();

  private subscriptions = new Subscription();

  constructor() {}

  ngOnInit(): void {
    this.makeSubscriptions();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  public onIsPoznanOnlyChange(toggleChange: boolean) {
    if (toggleChange) {
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

  private makeSubscriptions() {
    this.subscriptions.add(
      this.optionalInfoFormGroup.controls[
        'isPoznanOnly'
      ].valueChanges.subscribe(this.onIsPoznanOnlyChange.bind(this))
    );
    this.subscriptions.add(
      this.optionalInfoFormGroup
        .get('benefits')
        ?.valueChanges.subscribe(this.onBenefitsChange.bind(this))
    );
  }

  private onBenefitsChange() {
    const otherBenefitsCtrl = this.optionalInfoFormGroup.get('otherBenefits');
    if (this.isOtherOptionSelected) {
      otherBenefitsCtrl?.addValidators([
        Validators.required,
        Validators.maxLength(500),
      ]);
    } else {
      otherBenefitsCtrl?.clearValidators();
      otherBenefitsCtrl?.patchValue(null);
    }
    otherBenefitsCtrl?.updateValueAndValidity();
  }

  public get isOtherOptionSelected() {
    return !isNil(
      (<number[]>this.optionalInfoFormGroup.controls['benefits']?.value)?.find(
        selectedId => selectedId === -1
      )
    );
  }

  protected readonly AdvertisementCrudOperationType =
    AdvertisementCrudOperationType;
  protected readonly countCharacters = countCharacters;
}
