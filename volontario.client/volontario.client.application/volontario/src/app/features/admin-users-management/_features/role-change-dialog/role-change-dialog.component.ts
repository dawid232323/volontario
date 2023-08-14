import { Component, Inject, OnInit } from '@angular/core';
import { UserRole, UserRoles } from 'src/app/core/model/user-role.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface RoleChangeDialogDataIf {
  selectedRoles: number[];
}

@Component({
  selector: 'app-role-change-dialog',
  templateUrl: './role-change-dialog.component.html',
  styleUrls: ['./role-change-dialog.component.scss'],
})
export class RoleChangeDialogComponent implements OnInit {
  private _userRoles: UserRole[] = [];
  private _selectedRoles: number[] = [];

  constructor(
    public dialogRef: MatDialogRef<RoleChangeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RoleChangeDialogDataIf
  ) {}

  ngOnInit(): void {
    this._userRoles = UserRoles.getAllRoles();
    this._selectedRoles = this.data.selectedRoles;
  }

  public onModalClose() {
    const finalData: RoleChangeDialogDataIf = {
      selectedRoles: this._selectedRoles,
    };
    this.dialogRef.close(finalData);
  }

  public onModalCancel() {
    this.dialogRef.close();
  }

  public get userRoles(): UserRole[] {
    return this._userRoles;
  }

  public get selectedRoles(): number[] {
    return this._selectedRoles;
  }

  public set selectedRoles(roles: number[]) {
    this._selectedRoles = roles;
  }
}
