import { DictValuesOperationPerformerInterface } from 'src/app/features/manage-dict-values/_utils/dict-values-operation-performer.interface';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';
import { DictionaryValuesServiceInterface } from 'src/app/core/interface/dictionary-values-service.interface';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import {
  DictionaryValueTypeEnum,
  DictValueOperationInterface,
  DictValueOperationTypeEnum,
} from 'src/app/features/manage-dict-values/_features/dict-values-list/dict-values-list.component';
import {
  ConfirmationAlertComponent,
  ConfirmationAlertResult,
  ConfirmationAlertResultIf,
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import { Injectable } from '@angular/core';
import { DictValueFormComponent } from 'src/app/features/manage-dict-values/_features/dict-value-form/dict-value-form.component';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { VolunteerExperienceService } from 'src/app/core/service/volunteer-experience.service';
import { OfferBenefitService } from 'src/app/core/service/offer-benefit.service';

@Injectable()
export class DictValuesModalFactory {
  constructor(
    private interestCategoryService: InterestCategoryService,
    private offerBenefitService: OfferBenefitService,
    private experienceService: VolunteerExperienceService,
    private dialog: MatDialog
  ) {}

  public getOperationPerformer(
    valueType: DictionaryValueTypeEnum
  ):
    | DictValuesOperationPerformerInterface<DictionaryValueInterface>
    | undefined {
    switch (valueType) {
      case DictionaryValueTypeEnum.InterestCategory:
        return new InterestCategoryOperationPerformer(
          this.interestCategoryService,
          this.dialog
        );
      case DictionaryValueTypeEnum.AddBenefits:
        return new OfferBenefitsOperationPerformer(
          this.offerBenefitService,
          this.dialog
        );
      case DictionaryValueTypeEnum.ExpLevel:
        return new ExperienceLevelOperationPerformer(
          this.experienceService,
          this.dialog
        );
    }
    return undefined;
  }
}

abstract class AbstractDictValueOperationPerformer
  implements DictValuesOperationPerformerInterface<DictionaryValueInterface>
{
  performerService: DictionaryValuesServiceInterface<DictionaryValueInterface>;
  dialog: MatDialog;

  constructor(
    perfService: DictionaryValuesServiceInterface<DictionaryValueInterface>,
    dialog: MatDialog
  ) {
    this.performerService = perfService;
    this.dialog = dialog;
  }

  getDialogRef(
    operationDetails: DictValueOperationInterface
  ): MatDialogRef<any> | undefined {
    switch (operationDetails.operationType) {
      case DictValueOperationTypeEnum.Add:
        return this.getCreateModalRef(operationDetails);
      case DictValueOperationTypeEnum.Edit:
        return this.getEditModalRef(operationDetails);
      default:
        return this.getDeleteModalRef();
    }
  }

  public getOperationObservable(
    modalResult: DictionaryValueInterface,
    operationType: DictValueOperationTypeEnum,
    confirmationResult?: ConfirmationAlertResultIf
  ): Observable<any> | undefined {
    if (
      operationType === DictValueOperationTypeEnum.Delete &&
      confirmationResult?.confirmationAlertResult ===
        ConfirmationAlertResult.Accept
    ) {
      return this.getDeleteCallback(modalResult);
    }
    if (operationType === DictValueOperationTypeEnum.Add) {
      return this.performerService.createValue(modalResult);
    }
    if (operationType === DictValueOperationTypeEnum.Edit) {
      return this.performerService.updateValue(modalResult, modalResult.id);
    }
    return undefined;
  }

  protected getCreateModalRef(
    operationDetails: DictValueOperationInterface
  ): MatDialogRef<any> {
    return this.dialog.open(DictValueFormComponent, { data: operationDetails });
  }

  protected getEditModalRef(
    operationDetails: DictValueOperationInterface
  ): MatDialogRef<any> {
    return this.dialog.open(DictValueFormComponent, { data: operationDetails });
  }

  protected getDeleteModalRef(): MatDialogRef<any> {
    return this.dialog.open(ConfirmationAlertComponent);
  }

  protected getDeleteCallback(
    operationDetails: DictionaryValueInterface
  ): Observable<any> {
    return this.performerService.activateDeactivateValue(
      operationDetails?.isUsed,
      operationDetails?.id!
    );
  }
}

class InterestCategoryOperationPerformer extends AbstractDictValueOperationPerformer {}

class ExperienceLevelOperationPerformer extends AbstractDictValueOperationPerformer {}

class OfferBenefitsOperationPerformer extends AbstractDictValueOperationPerformer {}
