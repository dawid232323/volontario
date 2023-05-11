import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/core/model/user.model';
import * as moment from 'moment';
import {
  AdvertisementType,
  AdvertisementTypeEnum,
} from 'src/app/core/model/advertisement.model';
import { MatSelectChange } from '@angular/material/select';
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
    this.basicInfoFormGroup.controls['interval'].disable({ onlySelf: true });
    this.userService.getCurrentUserData().subscribe(currentUser => {
      if (!currentUser.hasUserRole(UserRoleEnum.InstitutionAdmin)) {
        this.basicInfoFormGroup.controls['contactPerson'].disable({
          onlySelf: true,
        });
      }
    });
  }

  public onAdvertisementTypeChange(selectedValue: number) {
    if (selectedValue === AdvertisementTypeEnum.Continuous) {
      this.basicInfoFormGroup.controls['endDate'].disable({ onlySelf: true });
      this.basicInfoFormGroup.controls['endDate'].removeValidators([
        Validators.required,
      ]);
      this.basicInfoFormGroup.controls['interval'].enable({ onlySelf: true });
      this.basicInfoFormGroup.controls['interval'].addValidators([
        Validators.required,
      ]);
    } else {
      this.basicInfoFormGroup.controls['endDate'].enable({ onlySelf: true });
      this.basicInfoFormGroup.controls['endDate'].addValidators([
        Validators.required,
      ]);
      this.basicInfoFormGroup.controls['interval'].disable({ onlySelf: true });
      this.basicInfoFormGroup.controls['interval'].removeValidators([
        Validators.required,
      ]);
    }
  }

  public get daysOfWeek(): string[] {
    const weekStart = moment().startOf('week');
    const days: string[] = [];

    for (let i = 0; i < 7; i++) {
      const dayOfWeek = weekStart.add(1, 'days').locale('pl').format('dddd');
      const firstLetter = dayOfWeek.substring(0, 1).toUpperCase();
      const replacedDay = firstLetter.concat(dayOfWeek.substring(1));
      days.push(replacedDay);
    }
    return days;
  }

  protected readonly DateValidatorUsageEnum = DateValidatorUsageEnum;
}
