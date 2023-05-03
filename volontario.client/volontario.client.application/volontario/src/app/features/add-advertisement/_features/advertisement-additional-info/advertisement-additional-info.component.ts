import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { isNil } from 'lodash';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-advertisement-additional-info',
  templateUrl: './advertisement-additional-info.component.html',
  styleUrls: ['./advertisement-additional-info.component.scss'],
})
export class AdvertisementAdditionalInfoComponent implements OnInit {
  @Input() additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  @Input() experienceLevels: VolunteerExperience[] = [];
  @Input() interestCategories: InterestCategoryDTO[] = [];
  constructor() {}

  ngOnInit(): void {}

  public get maxDescriptionLength(): any {
    const validatorMessage =
      this.additionalInfoFormGroup.controls['description'].errors?.[
        'maxlength'
      ];
    return validatorMessage?.['requiredLength'];
  }

  public onExperienceRequiredChange(toggleChange: MatSlideToggleChange) {
    if (toggleChange.checked) {
      this.additionalInfoFormGroup.controls['experienceLevel'].addValidators([
        Validators.required,
      ]);
    } else {
      this.additionalInfoFormGroup.controls['experienceLevel'].removeValidators(
        [Validators.required]
      );
    }
  }

  protected readonly console = console;
  protected readonly isNil = isNil;
}
