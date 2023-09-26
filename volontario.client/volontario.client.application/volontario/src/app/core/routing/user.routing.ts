import { Routes } from '@angular/router';
import { UserDetailsComponent } from 'src/app/features/user-details/user-details.component';
import { RouterGuard } from 'src/app/core/guard/router.guard';

export const userRouting: Routes = [
  {
    path: 'user/:user_id',
    component: UserDetailsComponent,
    canActivate: [RouterGuard],
  },
];
