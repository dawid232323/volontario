import { Routes } from '@angular/router';
import { ManageDictValuesComponent } from 'src/app/features/manage-dict-values/manage-dict-values.component';
import { RouterGuard } from 'src/app/core/guard/router.guard';
import { RoleDependentGuard } from 'src/app/core/guard/role-dependent.guard';
import { UserRole, UserRoleEnum } from 'src/app/core/model/user-role.model';

export const moderatorRouting: Routes = [
  {
    path: 'moderator',
    children: [
      {
        path: 'manage-dict-values',
        component: ManageDictValuesComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [UserRoleEnum.Moderator, UserRoleEnum.Admin],
        },
      },
    ],
  },
];
