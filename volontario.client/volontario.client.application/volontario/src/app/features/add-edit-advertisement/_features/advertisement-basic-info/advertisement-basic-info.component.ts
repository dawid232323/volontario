import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { InstitutionWorker } from 'src/app/core/model/user.model';
import { AdvertisementType, AdvertisementTypeEnum } from 'src/app/core/model/advertisement.model';
import { DateValidatorUsageEnum } from 'src/app/utils/validator.utils';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-advertisement-basic-info',
  templateUrl: './advertisement-basic-info.component.html',
  styleUrls: ['./advertisement-basic-info.component.scss'],
})
export class AdvertisementBasicInfoComponent implements OnInit, OnDestroy {
  @Input() basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  @Input() institutionWorkers: InstitutionWorker[] = [];
  @Input() advertisementTypes: AdvertisementType[] = [];
  @Input() canSelectUser = false;

  public isDescriptionOfCycleHidden = true;

  private subscriptions = new Subscription();

  constructor() {}

  ngOnInit(): void {
    this.subscriptions.add(this.basicInfoFormGroup.controls['advertisementType'].valueChanges.subscribe(this.onAdvertisementTypeChange.bind(this)));
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  public onAdvertisementTypeChange(selectedValue: number) {
    this.isDescriptionOfCycleHidden = selectedValue === AdvertisementTypeEnum.SingleUse;
    if (this.isDescriptionOfCycleHidden) {
      this.basicInfoFormGroup.controls['periodicDescription'].removeValidators(Validators.required);
    } else {
      this.basicInfoFormGroup.controls['periodicDescription'].addValidators(Validators.required);
    }
  }

  protected readonly DateValidatorUsageEnum = DateValidatorUsageEnum;
}
