import { Routes } from '@angular/router';
import { AdminUsersManagementComponent } from 'src/app/features/admin-users-management/admin-users-management.component';
import { RouterGuard } from 'src/app/core/guard/router.guard';
import { RoleDependentGuard } from 'src/app/core/guard/role-dependent.guard';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';

export const adminRouting: Routes = [
  {
    path: 'admin',
    children: [
      {
        path: 'users',
        component: AdminUsersManagementComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [UserRoleEnum.Admin],
        },
      },
    ],
  },
];
