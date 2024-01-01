import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import {
  EvaluationAvailableOffer,
  InstitutionOfferEvaluation,
  OfferEvaluationIf,
  UserEvaluation,
  UserOfferEvaluation,
} from 'src/app/core/model/evaluation.model';
import { MatDialog } from '@angular/material/dialog';
import {
  EvaluationModalComponent,
  EvaluationModalData,
  EvaluationModalType,
} from 'src/app/shared/features/evaluation-modal/evaluation-modal.component';
import { isNil } from 'lodash';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import {
  ConfirmationAlertComponent,
  ConfirmationAlertResult,
  ConfirmationAlertResultIf,
} from '../confirmation-alert/confirmation-alert.component';
import { EvaluationService } from '../../../core/service/evaluation.service';
import { EvaluationRemovalDataIf } from '../evaluation-card/evaluation-card.component';

@Component({
  selector: 'app-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.scss'],
})
export class EvaluationComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  @Input() canPerformEvaluation = false;

  @Input() set evaluations(evaluation: UserEvaluation | undefined) {
    if (isNil(evaluation)) {
      return;
    }
    this._evaluations = evaluation;
    this._displayedEvaluations = evaluation.evaluations.slice(
      0,
      this.paginator?.pageSize - 1 || 6
    );
    if (!isNil(this.paginator)) {
      this.paginator.pageIndex = 0;
    }
  }

  @Input() availableOffers: EvaluationAvailableOffer[] = [];
  @Input() evaluationMaxScale: number = 5;

  @Output() evaluationPerformed: EventEmitter<EvaluationModalData> =
    new EventEmitter<EvaluationModalData>();

  public selectedPageIndex = 1;

  private _displayedEvaluations: OfferEvaluationIf[] = [];
  private _evaluations!: UserEvaluation;

  constructor(
    private matDialog: MatDialog,
    private evaluationService: EvaluationService
  ) {}

  ngOnInit(): void {}

  public onUserRateButtonClicked() {
    if (!this.canPerformEvaluation) {
      return;
    }
    const data: EvaluationModalData = {
      availableOffers: this.availableOffers,
      evaluationMaxScale: this.evaluationMaxScale,
      modalType: EvaluationModalType.New,
    };
    const dialogRef = this.matDialog.open(EvaluationModalComponent, {
      data: data,
      width: '40rem',
    });
    dialogRef
      .afterClosed()
      .subscribe((modalResult: EvaluationModalData | null) => {
        if (!isNil(modalResult)) {
          this.evaluationPerformed.emit(modalResult);
        }
      });
  }

  public onPageEvent(event: PageEvent): void {
    if (isNil(this.evaluations)) {
      return;
    }
    const startIndex = event.pageSize * event.pageIndex;
    const endIndex = startIndex + event.pageSize;
    this._displayedEvaluations = this.evaluations.evaluations.slice(
      startIndex,
      endIndex
    );
  }

  public get evaluations(): UserEvaluation | undefined {
    return this._evaluations;
  }

  public get displayedEvaluations(): OfferEvaluationIf[] {
    return this._displayedEvaluations;
  }

  public get hasNoEvaluations(): boolean {
    if (isNil(this.evaluations) || isNil(this.displayedEvaluations)) {
      return true;
    }
    return (
      this._evaluations.evaluations.length === 0 &&
      this._displayedEvaluations.length === 0
    );
  }

  removeEvaluation(evaluationRemovalData: EvaluationRemovalDataIf): void {
    this.matDialog
      .open(ConfirmationAlertComponent, {
        data: {
          confirmationMessage: 'Czy na pewno chcesz usunąć ocenę?',
        },
      })
      .afterClosed()
      .subscribe((result: ConfirmationAlertResultIf) => {
        if (result.confirmationAlertResult === ConfirmationAlertResult.Accept) {
          this.removeEvaluations(evaluationRemovalData.evaluation);
          this.updateAverageEvaluation();

          if (evaluationRemovalData.evaluation instanceof UserOfferEvaluation) {
            this.removeVolunteerRating(
              Number(evaluationRemovalData.volunteerId),
              evaluationRemovalData.offerId
            );
          } else if (
            evaluationRemovalData.evaluation instanceof
            InstitutionOfferEvaluation
          ) {
            this.removeInstitutionRating(
              Number(evaluationRemovalData.volunteerId),
              evaluationRemovalData.offerId
            );
          }
        }
      });
  }

  editEvaluation(offerId: number) {
    const selectedRating = this._evaluations.evaluations.find(
      evaluation => evaluation.offerId === offerId
    );
    if (isNil(selectedRating)) {
      return;
    }
    const data: EvaluationModalData = {
      availableOffers: this.availableOffers,
      evaluationMaxScale: this.evaluationMaxScale,
      modalType: EvaluationModalType.Edit,
      selectedOfferId: offerId,
      evaluationValue: selectedRating.evaluationValue,
      comment: selectedRating.evaluationComment,
    };
    this.matDialog
      .open(EvaluationModalComponent, {
        data: data,
        width: '40rem',
      })
      .afterClosed()
      .subscribe((modalResult: EvaluationModalData | null) => {
        if (!isNil(modalResult)) {
          this.evaluationPerformed.emit({
            ...modalResult,
            modalType: EvaluationModalType.Edit,
          });
        }
      });
  }

  private removeEvaluations(offerEvaluation: OfferEvaluationIf): void {
    const displayedEvaluationIndex: number =
      this._displayedEvaluations.indexOf(offerEvaluation);
    const evaluationIndex: number =
      this._evaluations.evaluations.indexOf(offerEvaluation);

    this._displayedEvaluations.splice(displayedEvaluationIndex, 1);
    this._evaluations.evaluations.splice(evaluationIndex, 1);
  }

  private updateAverageEvaluation(): void {
    this._evaluations.evaluationAverage =
      this._evaluations.evaluations
        .map((evaluation: OfferEvaluationIf) => evaluation.evaluationValue)
        .reduce((accumulator, currentValue) => accumulator + currentValue, 0) /
      this._evaluations.evaluations.length;
  }

  private removeVolunteerRating(volunteerId: number, offerId: number): void {
    this.evaluationService
      .deleteVolunteerRating(volunteerId, offerId)
      .subscribe();
  }

  private removeInstitutionRating(volunteerId: number, offerId: number): void {
    this.evaluationService
      .deleteInstitutionRating(volunteerId, offerId)
      .subscribe();
  }

  protected readonly Array = Array;
}
