import { Component, Inject, OnInit } from '@angular/core';
import { PresenceStateEnum } from 'src/app/core/model/offer-presence.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface VolConfirmPresenceInitialData {
  canPostponeReminder: boolean;
}

@Component({
  selector: 'app-volunteer-confirm-presence',
  templateUrl: './volunteer-confirm-presence.component.html',
  styleUrls: ['./volunteer-confirm-presence.component.scss'],
})
export class VolunteerConfirmPresenceComponent implements OnInit {
  private _canPostponeReminder = false;

  constructor(
    private dialogRef: MatDialogRef<VolunteerConfirmPresenceComponent>,
    @Inject(MAT_DIALOG_DATA) private initialData: VolConfirmPresenceInitialData
  ) {
    this._canPostponeReminder = initialData.canPostponeReminder;
  }

  ngOnInit(): void {}

  public onPostponePresence() {
    this.dialogRef.close(PresenceStateEnum.Unresolved);
  }

  public onConfirmDenyPresence(decision: PresenceStateEnum) {
    this.dialogRef.close(decision);
  }

  public get canPostponeReminder(): boolean {
    return this._canPostponeReminder;
  }

  protected readonly PresenceStateEnum = PresenceStateEnum;
}
