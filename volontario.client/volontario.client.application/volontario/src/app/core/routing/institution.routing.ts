import { Routes } from '@angular/router';
import { InstitutionVerifyComponent } from 'src/app/features/institution-verify/institution-verify.component';
import {
  InstitutionRegistrationGuard,
  InstitutionRegistrationStage,
} from 'src/app/core/guard/institution-registration.guard';
import { RegisterInstitutionComponent } from 'src/app/features/register-institution/register-institution.component';
import {
  EmployeeRegistrationModeEnum,
  RegisterContactPersonComponent,
} from 'src/app/features/register-contact-person/register-contact-person.component';
import { InstitutionAdvertisementPanelComponent } from 'src/app/features/institution-advertisement-panel/institution-advertisement-panel.component';
import { RouterGuard } from 'src/app/core/guard/router.guard';
import { RoleDependentGuard } from 'src/app/core/guard/role-dependent.guard';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { InstitutionDetailsComponent } from 'src/app/features/institution-details/institution-details.component';
import { InstitutionEditComponent } from 'src/app/features/institution-edit/institution-edit.component';
import { InstitutionAdminGuard } from 'src/app/core/guard/institution-admin.guard';
import { ManageInstitutionWorkersComponent } from 'src/app/features/manage-institution-workers/manage-institution-workers.component';
import { InstitutionOfferPresenceComponent } from 'src/app/features/institution-offer-presence/institution-offer-presence.component';
import { OfferPresenceGuard } from 'src/app/core/guard/offer-presence.guard';

export const institutionRouting: Routes = [
  {
    path: 'register-employee/:institution_id',
    component: RegisterContactPersonComponent,
    canActivate: [InstitutionRegistrationGuard],
    data: {
      stage: InstitutionRegistrationStage.ContactPersonRegistration,
      mode: EmployeeRegistrationModeEnum.RegisterEmployee,
    },
  },
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
        data: {
          stage: InstitutionRegistrationStage.ContactPersonRegistration,
          mode: EmployeeRegistrationModeEnum.RegisterContactPerson,
        },
      },
      {
        path: 'advertisement-panel',
        component: InstitutionAdvertisementPanelComponent,
        canActivate: [RouterGuard, RoleDependentGuard],
        data: {
          roles: [
            UserRoleEnum.InstitutionWorker,
            UserRoleEnum.InstitutionAdmin,
          ],
        },
      },
      {
        path: ':institution_id/confirm-presence',
        component: InstitutionOfferPresenceComponent,
        canActivate: [RouterGuard, RoleDependentGuard, OfferPresenceGuard],
        data: {
          roles: [
            UserRoleEnum.InstitutionWorker,
            UserRoleEnum.InstitutionAdmin,
          ],
        },
      },
      {
        path: ':institution_id',
        component: InstitutionDetailsComponent,
        canActivate: [RouterGuard],
      },
      {
        path: 'edit/:institution_id',
        component: InstitutionEditComponent,
        canActivate: [RouterGuard, InstitutionAdminGuard],
      },
      {
        path: 'workers/:institution_id',
        component: ManageInstitutionWorkersComponent,
        canActivate: [RouterGuard, InstitutionAdminGuard],
      },
    ],
  },
];
