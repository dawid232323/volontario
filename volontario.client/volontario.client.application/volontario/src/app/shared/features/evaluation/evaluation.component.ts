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
  OfferEvaluationIf,
  UserEvaluation,
} from 'src/app/core/model/evaluation.model';
import { MatDialog } from '@angular/material/dialog';
import {
  EvaluationModalComponent,
  EvaluationModalData,
} from 'src/app/shared/features/evaluation-modal/evaluation-modal.component';
import { isNil } from 'lodash';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

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
  constructor(private matDialog: MatDialog) {}

  ngOnInit(): void {}

  public onUserRateButtonClicked() {
    if (!this.canPerformEvaluation) {
      return;
    }
    const data: EvaluationModalData = {
      availableOffers: this.availableOffers,
      evaluationMaxScale: this.evaluationMaxScale,
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

  protected readonly Array = Array;
}
