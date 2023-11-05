import { Routes } from '@angular/router';
import { UserDetailsComponent } from 'src/app/features/user-details/user-details.component';
import { RouterGuard } from 'src/app/core/guard/router.guard';
import { UserEditDataComponent } from '../../features/user-edit-data/user-edit-data.component';
import { CanEditDataGuard } from '../guard/can-edit-data.guard';

export const userRouting: Routes = [
  {
    path: 'user/:user_id',
    canActivate: [RouterGuard],
    children: [
      {
        path: '',
        component: UserDetailsComponent,
      },
      {
        path: 'edit-data',
        canActivate: [CanEditDataGuard],
        component: UserEditDataComponent,
      },
    ],
  },
];
