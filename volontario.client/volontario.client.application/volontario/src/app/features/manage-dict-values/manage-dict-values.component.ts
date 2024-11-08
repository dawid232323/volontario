import { Component, OnInit, ViewChild } from '@angular/core';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
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
import {
  ConfirmationAlertResult,
  ConfirmationAlertResultIf,
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
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

  private _interestCategories: InterestCategoryDTO[] = [];
  private _offerBenefits: AdvertisementBenefit[] = [];
  private _isLoadingData = true;

  constructor(
    private interestCategoryService: InterestCategoryService,
    private offerBenefitService: OfferBenefitService,
    private operationPerformerFactory: DictValuesModalFactory
  ) {}

  ngOnInit(): void {
    this.setInitialData();
  }

  public get interestCategories(): InterestCategoryDTO[] {
    return this._interestCategories;
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
      .subscribe(
        (result: ConfirmationAlertResultIf | DictionaryValueInterface) =>
          this.onModalDialogAfterClosed(
            result,
            operationDetails,
            operationPerformer
          )
      );
  }

  private isConfirmationAlertResult(
    result: ConfirmationAlertResultIf | DictionaryValueInterface
  ): result is ConfirmationAlertResultIf {
    return (<DictionaryValueInterface>result).name === undefined;
  }
  private setInitialData() {
    this._isLoadingData = true;
    forkJoin([
      this.interestCategoryService.getAllValues(),
      this.offerBenefitService.getAllValues(),
    ]).subscribe(([categories, benefits]) => {
      this._interestCategories = categories;
      this._offerBenefits = benefits;
      this._isLoadingData = false;
    });
  }

  private onModalDialogAfterClosed(
    result: ConfirmationAlertResultIf | DictionaryValueInterface,
    operationDetails: DictValueOperationInterface,
    operationPerformer: DictValuesOperationPerformerInterface<DictionaryValueInterface>
  ) {
    let operationObservable: Observable<any> | undefined;
    if (
      this.isConfirmationAlertResult(result) &&
      result.confirmationAlertResult !== ConfirmationAlertResult.Accept
    ) {
      return;
    }
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
    }
  }

  protected readonly DictionaryValueTypeEnum = DictionaryValueTypeEnum;
}
