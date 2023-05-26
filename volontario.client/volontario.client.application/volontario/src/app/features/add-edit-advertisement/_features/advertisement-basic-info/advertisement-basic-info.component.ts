import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/core/model/user.model';
import * as moment from 'moment';
import {
  AdvertisementType,
  AdvertisementTypeEnum,
} from 'src/app/core/model/advertisement.model';
import { UserService } from 'src/app/core/service/user.service';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { DateValidatorUsageEnum } from 'src/app/utils/validator.utils';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-advertisement-basic-info',
  templateUrl: './advertisement-basic-info.component.html',
  styleUrls: ['./advertisement-basic-info.component.scss'],
})
export class AdvertisementBasicInfoComponent implements OnInit, OnDestroy {
  @Input() basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  @Input() institutionWorkers: User[] = [];
  @Input() advertisementTypes: AdvertisementType[] = [];

  public isDescriptionOfCycleHidden = true;

  private subscriptions = new Subscription();

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.setInitialFormData();
    this.subscriptions.add(
      this.basicInfoFormGroup.controls[
        'advertisementType'
      ].valueChanges.subscribe(this.onAdvertisementTypeChange.bind(this))
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  private setInitialFormData() {
    this.userService.getCurrentUserData().subscribe(currentUser => {
      if (!currentUser.hasUserRole(UserRoleEnum.InstitutionAdmin)) {
        this.basicInfoFormGroup.controls['contactPerson'].disable({
          onlySelf: true,
        });
      }
    });
  }

  public onAdvertisementTypeChange(selectedValue: number) {
    this.isDescriptionOfCycleHidden =
      selectedValue === AdvertisementTypeEnum.SingleUse;
    if (this.isDescriptionOfCycleHidden) {
      this.basicInfoFormGroup.controls['periodicDescription'].removeValidators(
        Validators.required
      );
    } else {
      this.basicInfoFormGroup.controls['periodicDescription'].addValidators(
        Validators.required
      );
    }
  }

  protected readonly DateValidatorUsageEnum = DateValidatorUsageEnum;
}
