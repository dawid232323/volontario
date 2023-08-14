import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminUsersManagementComponent } from 'src/app/features/admin-users-management/admin-users-management.component';
import { UsersFilterPaneModule } from 'src/app/features/admin-users-management/_features/users-filter-pane/users-filter-pane.module';
import { UsersListModule } from 'src/app/features/admin-users-management/_features/users-list/users-list.module';
import { RoleChangeDialogModule } from 'src/app/features/admin-users-management/_features/role-change-dialog/role-change-dialog.module';
import { SetPasswordModule } from 'src/app/shared/features/set-password-form/set-password.module';

@NgModule({
  declarations: [AdminUsersManagementComponent],
  imports: [
    CommonModule,
    UsersFilterPaneModule,
    UsersListModule,
    RoleChangeDialogModule,
    SetPasswordModule,
  ],
  exports: [AdminUsersManagementComponent],
})
export class AdminUsersManagementModule {}
