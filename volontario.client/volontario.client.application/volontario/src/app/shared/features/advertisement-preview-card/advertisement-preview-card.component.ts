import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AdvertisementPreview } from 'src/app/core/model/advertisement.model';
import { isNil } from 'lodash';
import * as moment from 'moment';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import {
  ConfirmationAlertComponent,
  ConfirmationAlertInitialData,
  ConfirmationAlertResult,
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import { AdvertisementPreviewActionIf } from 'src/app/features/institution-advertisement-panel/institution-advertisement-panel.component';

@Component({
  selector: 'app-advertisement-preview-card',
  templateUrl: './advertisement-preview-card.component.html',
  styleUrls: [
    './advertisement-preview-card.component.scss',
    '../../styles/material-styles.scss',
  ],
})
export class AdvertisementPreviewCardComponent implements OnInit {
  @Input() advertisement: AdvertisementPreview | undefined = undefined;
  @Input() shouldShowContextMenu: boolean = false;

  @Output() visibilityChangedEvent =
    new EventEmitter<AdvertisementPreviewActionIf>();
  constructor(private router: Router, private matDialog: MatDialog) {}

  ngOnInit(): void {}

  public getFormattedDate(date?: Date): string {
    if (isNil(date)) {
      return '';
    }
    return moment(date).format('DD-MM-yyyy');
  }

  public goToEditAdvertisement() {
    this.router.navigate(['advertisement', 'edit', this.advertisement?.id]);
  }

  public onOfferVisibilityChange() {
    const dialogData: ConfirmationAlertInitialData = {
      confirmationMessage: this.confirmationMessage,
      confirmButtonLabel: this.confirmationConfirmLabel,
      cancelButtonLabel: this.cancelLabel,
    };
    const dialogRef = this.matDialog.open(ConfirmationAlertComponent, {
      data: dialogData,
    });
    dialogRef
      .afterClosed()
      .subscribe({ next: this.onAfterDialogClosed.bind(this) });
  }

  private onAfterDialogClosed(dialogResult: ConfirmationAlertResult) {
    if (dialogResult === ConfirmationAlertResult.Accept) {
      this.visibilityChangedEvent.emit({
        isHidden: !this.advertisement?.isHidden,
        advertisementId: this.advertisement!.id,
      });
      this.advertisement!.isHidden = !this.advertisement!.isHidden;
    }
  }

  private get confirmationMessage() {
    if (this?.advertisement?.isHidden) {
      return 'Ogłoszenie stanie się widoczne dla wszystkich użytkowników';
    }
    return 'Ogłoszenie przestanie być widoczne dla wszystkich użytkowników';
  }

  private get confirmationConfirmLabel() {
    if (this?.advertisement?.isHidden) {
      return 'Uwidocznij ogłoszenie';
    }
    return 'Ukryj ogłoszenie';
  }

  private get cancelLabel() {
    return 'Anuluj';
  }

  protected readonly undefined = undefined;
}
