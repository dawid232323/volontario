import { Component, OnInit, ViewChild } from '@angular/core';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { VolunteerExperienceService } from 'src/app/core/service/volunteer-experience.service';
import { AdvertisementService } from 'src/app/core/service/advertisement.service';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { VolunteerExperience } from 'src/app/core/model/volunteer-experience.model';
import { AdvertisementBenefit } from 'src/app/core/model/advertisement.model';
import { forkJoin, Observable } from 'rxjs';
import {
  DictionaryValueTypeEnum,
  DictValueOperationInterface,
  DictValuesListComponent,
} from 'src/app/features/manage-dict-values/_features/dict-values-list/dict-values-list.component';
import { DictValuesModalFactory } from 'src/app/features/manage-dict-values/_utils/dict-values-modal-factory';
import { isNil } from 'lodash';
import { OfferBenefitService } from 'src/app/core/service/offer-benefit.service';
import { ConfirmationAlertResult } from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';
import { DictValuesOperationPerformerInterface } from 'src/app/features/manage-dict-values/_utils/dict-values-operation-performer.interface';

@Component({
  selector: 'app-manage-dict-values',
  templateUrl: './manage-dict-values.component.html',
  styleUrls: ['./manage-dict-values.component.scss'],
  providers: [DictValuesModalFactory],
})
export class ManageDictValuesComponent implements OnInit {
  @ViewChild('interestCategoriesList')
  private _interestCategoryList?: DictValuesListComponent;
  @ViewChild('benefitsList')
  private _offerBenefitsList?: DictValuesListComponent;
  @ViewChild('experienceLevelsList')
  private _experienceLevelsList?: DictValuesListComponent;

  private _interestCategories: InterestCategoryDTO[] = [];
  private _volunteerExperience: VolunteerExperience[] = [];
  private _offerBenefits: AdvertisementBenefit[] = [];
  private _isLoadingData = true;

  constructor(
    private interestCategoryService: InterestCategoryService,
    private volunteerExpService: VolunteerExperienceService,
    private offerBenefitService: OfferBenefitService,
    private operationPerformerFactory: DictValuesModalFactory
  ) {}

  ngOnInit(): void {
    this.setInitialData();
  }

  public get interestCategories(): InterestCategoryDTO[] {
    return this._interestCategories;
  }

  public get volunteerExperience(): VolunteerExperience[] {
    return this._volunteerExperience;
  }

  public get offerBenefits(): AdvertisementBenefit[] {
    return this._offerBenefits;
  }

  public get isLoadingData(): boolean {
    return this._isLoadingData;
  }

  public onOperationAction(operationDetails: DictValueOperationInterface) {
    const operationPerformer =
      this.operationPerformerFactory.getOperationPerformer(
        operationDetails.valueType
      );
    if (isNil(operationPerformer)) {
      return;
    }
    const dialogRef = operationPerformer.getDialogRef(operationDetails);
    dialogRef
      ?.afterClosed()
      .subscribe((result: ConfirmationAlertResult | DictionaryValueInterface) =>
        this.onModalDialogAfterClosed(
          result,
          operationDetails,
          operationPerformer
        )
      );
  }

  private isConfirmationAlertResult(
    result: ConfirmationAlertResult | DictionaryValueInterface
  ): result is ConfirmationAlertResult {
    return (<DictionaryValueInterface>result).name === undefined;
  }
  private setInitialData() {
    this._isLoadingData = true;
    forkJoin([
      this.interestCategoryService.getAllValues(),
      this.volunteerExpService.getAllValues(),
      this.offerBenefitService.getAllValues(),
    ]).subscribe(([categories, expLevels, benefits]) => {
      this._interestCategories = categories;
      this._volunteerExperience = expLevels;
      this._offerBenefits = benefits;
      this._isLoadingData = false;
    });
  }

  private onModalDialogAfterClosed(
    result: ConfirmationAlertResult | DictionaryValueInterface,
    operationDetails: DictValueOperationInterface,
    operationPerformer: DictValuesOperationPerformerInterface<DictionaryValueInterface>
  ) {
    let operationObservable: Observable<any> | undefined;
    if (this.isConfirmationAlertResult(result)) {
      operationObservable = operationPerformer.getOperationObservable(
        operationDetails.operationData!,
        operationDetails.operationType,
        result
      );
    } else {
      operationObservable = operationPerformer.getOperationObservable(
        result,
        operationDetails.operationType
      );
    }
    if (isNil(operationObservable)) {
      return;
    }
    operationObservable.subscribe(r => {
      const body = this.isConfirmationAlertResult(result)
        ? operationDetails.operationData!
        : { ...result, id: r.id, isUsed: r.isUsed };
      if (this.isConfirmationAlertResult(result)) {
        body.isUsed = !body.isUsed;
      }
      this.onAfterValueUpdate(body, operationDetails.valueType);
    });
  }

  private onAfterValueUpdate(
    valueData: DictionaryValueInterface,
    valueType: DictionaryValueTypeEnum
  ) {
    switch (valueType) {
      case DictionaryValueTypeEnum.InterestCategory:
        this._interestCategoryList?.updateValue(valueData);
        break;
      case DictionaryValueTypeEnum.AddBenefits:
        this._offerBenefitsList?.updateValue(valueData);
        break;
      case DictionaryValueTypeEnum.ExpLevel:
        this._experienceLevelsList?.updateValue(valueData);
        break;
    }
  }

  protected readonly DictionaryValueTypeEnum = DictionaryValueTypeEnum;
}
