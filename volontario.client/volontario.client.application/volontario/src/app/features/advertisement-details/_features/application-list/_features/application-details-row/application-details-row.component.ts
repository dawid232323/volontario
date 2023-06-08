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
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';

@Component({
  selector: 'app-application-details-row',
  templateUrl: './application-details-row.component.html',
  styleUrls: ['./application-details-row.component.scss'],
})
export class ApplicationDetailsRowComponent implements OnInit {
  @Input() applicationDetails?: ApplicationDetails;
  @Output() applicationDetailsChange = new EventEmitter<ApplicationActionIf>();

  public shouldDisableButtons = false;

  constructor(private matModal: MatDialog) {}

  ngOnInit(): void {}

  public onDeclineButtonClicked() {
    const modalInitialData: ConfirmationAlertInitialData = {
      confirmationMessage: 'Odrzucenie aplikacji jest nieodwracalne',
      cancelButtonLabel: 'Anuluj',
      confirmButtonLabel: 'Odrzuć aplikację',
    };
    const confirmRef = this.matModal.open(ConfirmationAlertComponent, {
      data: modalInitialData,
    });
    confirmRef.afterClosed().subscribe(confirmationResult => {
      if (confirmationResult === ConfirmationAlertResult.Accept) {
        this.applicationDetails!.state = ApplicationStateEnumName.Rejected;
        this.emitChangeEvent(ApplicationActionTypeEnum.Reject);
      }
    });
  }

  public onAcceptButtonClicked() {
    this.applicationDetails!.state = ApplicationStateEnumName.Accepted;
    this.emitChangeEvent(ApplicationActionTypeEnum.Accept);
  }

  private emitChangeEvent(operationType: ApplicationActionTypeEnum) {
    this.shouldDisableButtons = true;
    this.applicationDetailsChange.emit({
      application: this.applicationDetails!,
      actionType: operationType,
    });
  }

  protected readonly ApplicationStateEnumName = ApplicationStateEnumName;
}
