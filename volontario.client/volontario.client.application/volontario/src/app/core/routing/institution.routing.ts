import { Routes } from '@angular/router';
import { InstitutionVerifyComponent } from 'src/app/features/institution-verify/institution-verify.component';
import { InstitutionRegistrationGuard, InstitutionRegistrationStage } from 'src/app/core/guard/institution-registration.guard';
import { RegisterInstitutionComponent } from 'src/app/features/register-institution/register-institution.component';
import { RegisterContactPersonComponent } from 'src/app/features/register-contact-person/register-contact-person.component';
import { InstitutionAdvertisementPanelComponent } from 'src/app/features/institution-advertisement-panel/institution-advertisement-panel.component';
import { RouterGuard } from 'src/app/core/guard/router.guard';
import { RoleDependentGuard } from 'src/app/core/guard/role-dependent.guard';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { InstitutionDetailsComponent } from 'src/app/features/institution-details/institution-details.component';
import { InstitutionEditComponent } from 'src/app/features/institution-edit/institution-edit.component';
import { InstitutionAdminGuard } from 'src/app/core/guard/institution-admin.guard';

export const institutionRouting: Routes = [
  {
    path: 'institution',
    children: [
      {
        path: 'verify',
        component: InstitutionVerifyComponent,
        canActivate: [InstitutionRegistrationGuard],
        data: { stage: InstitutionRegistrationStage.Verification },
      },
      { path: 'register', component: RegisterInstitutionComponent },
      {
        path: 'register-contact-person',
        component: RegisterContactPersonComponent,
        canActivate: [InstitutionRegistrationGuard],
        data: { stage: InstitutionRegistrationStage.ContactPersonRegistration },
      },
      {
        path: 'advertisement-panel',
        component: InstitutionAdvertisementPanelComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [UserRoleEnum.InstitutionWorker, UserRoleEnum.InstitutionAdmin],
        },
      },
      {
        path: ':institution_id',
        component: InstitutionDetailsComponent,
        canActivate: [RouterGuard],
      },
      { path: 'edit/:institution_id', component: InstitutionEditComponent, canActivate: [RouterGuard, InstitutionAdminGuard] },
    ],
  },
];
