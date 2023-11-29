import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  ApplicationDetails,
  ApplicationStateEnumName,
} from 'src/app/core/model/application.model';
import {
  ApplicationActionIf,
  ApplicationActionTypeEnum,
} from 'src/app/core/interface/application.interface';
import { MatDialog } from '@angular/material/dialog';
import {
  ConfirmationAlertComponent,
  ConfirmationAlertInitialData,
  ConfirmationAlertResult,
  ConfirmationAlertResultIf,
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import { Router } from '@angular/router';
import { isNil } from 'lodash';

@Component({
  selector: 'app-application-details-row',
  templateUrl: './application-details-row.component.html',
  styleUrls: ['./application-details-row.component.scss'],
})
export class ApplicationDetailsRowComponent implements OnInit {
  @Input() applicationDetails?: ApplicationDetails;
  @Output() applicationDetailsChange = new EventEmitter<ApplicationActionIf>();

  public shouldDisableButtons = false;
  public shouldAnonymizeEmail: boolean = true;
  public shouldAnonymizePhone: boolean = true;

  constructor(private matModal: MatDialog, private router: Router) {}

  ngOnInit(): void {}

  public onDeclineButtonClicked() {
    const modalInitialData: ConfirmationAlertInitialData = {
      confirmationMessage: 'Odrzucenie aplikacji jest nieodwracalne',
      cancelButtonLabel: 'Anuluj',
      confirmButtonLabel: 'Odrzuć aplikację',
      shouldAskForReason: true,
    };
    const confirmRef = this.matModal.open(ConfirmationAlertComponent, {
      data: modalInitialData,
    });
    confirmRef
      .afterClosed()
      .subscribe((confirmationResult: ConfirmationAlertResultIf) => {
        if (
          confirmationResult?.confirmationAlertResult ===
          ConfirmationAlertResult.Accept
        ) {
          this.applicationDetails!.state = ApplicationStateEnumName.Rejected;
          this.emitChangeEvent(
            ApplicationActionTypeEnum.Reject,
            confirmationResult.resultReason
          );
        }
      });
  }

  public onAcceptButtonClicked() {
    this.applicationDetails!.state = ApplicationStateEnumName.UnderRecruitment;
    this.emitChangeEvent(ApplicationActionTypeEnum.Accept);
  }

  public onReserveListButtonClicked() {
    this.applicationDetails!.state = ApplicationStateEnumName.Reserve_list;
    this.emitChangeEvent(ApplicationActionTypeEnum.Reserve_list);
  }

  private emitChangeEvent(
    operationType: ApplicationActionTypeEnum,
    reason?: string
  ) {
    this.shouldDisableButtons = true;
    this.applicationDetailsChange.emit({
      application: this.applicationDetails!,
      actionType: operationType,
      actionReason: reason,
    });
  }

  public onShowVolunteerDetails() {
    if (isNil(this.applicationDetails?.assignedPersonId)) {
      return;
    }
    this.router.navigate(['user', this.applicationDetails?.volunteerId]);
  }

  protected readonly ApplicationStateEnumName = ApplicationStateEnumName;
}
