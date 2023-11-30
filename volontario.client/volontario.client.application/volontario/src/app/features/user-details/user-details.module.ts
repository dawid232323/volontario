import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserDetailsComponent } from './user-details.component';
import { MatTabsModule } from '@angular/material/tabs';
import { BasicUserDetailsModule } from 'src/app/features/user-details/_features/basic-user-details/basic-user-details.module';
import { UserEvaluationModule } from 'src/app/shared/features/evaluation/user-evaluation.module';

@NgModule({
  declarations: [UserDetailsComponent],
  imports: [
    CommonModule,
    MatTabsModule,
    BasicUserDetailsModule,
    UserEvaluationModule,
  ],
  exports: [UserDetailsComponent],
})
export class UserDetailsModule {}
