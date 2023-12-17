import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { isNil } from 'lodash';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { DefaultAngularEditorConfigProvider } from 'src/app/utils/angular-editor.const';
import { Subscription } from 'rxjs';
import { countCharacters } from 'src/app/utils/validator.utils';

@Component({
  selector: 'app-advertisement-additional-info',
  templateUrl: './advertisement-additional-info.component.html',
  styleUrls: ['./advertisement-additional-info.component.scss'],
})
export class AdvertisementAdditionalInfoComponent implements OnInit, OnDestroy {
  @Input() additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  @Input() interestCategories: InterestCategoryDTO[] = [];

  private subscription = new Subscription();
  constructor() {}

  ngOnInit(): void {
    this.makeSubscription();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
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

  private makeSubscription() {
    this.subscription.add(
      this.additionalInfoFormGroup.controls[
        'advertisementCategories'
      ]?.valueChanges.subscribe(this.onCategoriesSelectionChange.bind(this))
    );
  }

  private onCategoriesSelectionChange() {
    const otherCategoriesCtrl =
      this.additionalInfoFormGroup.controls['otherCategories'];
    if (this.isOtherCategoriesSelected) {
      otherCategoriesCtrl?.addValidators([
        Validators.required,
        Validators.maxLength(500),
      ]);
    } else {
      otherCategoriesCtrl?.clearValidators();
      otherCategoriesCtrl?.patchValue(null);
    }
    otherCategoriesCtrl?.updateValueAndValidity();
  }

  public get isOtherCategoriesSelected(): boolean {
    return !isNil(
      (<number[]>(
        this.additionalInfoFormGroup.controls['advertisementCategories']?.value
      ))?.find(selectedId => selectedId === -1)
    );
  }

  protected readonly isNil = isNil;
  protected readonly defaultAngularEditorConfig =
    new DefaultAngularEditorConfigProvider().config;
  protected readonly countCharacters = countCharacters;
}
