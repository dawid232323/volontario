import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManageDictValuesComponent } from './manage-dict-values.component';
import { MatTabsModule } from '@angular/material/tabs';
import { DictValuesListModule } from 'src/app/features/manage-dict-values/_features/dict-values-list/dict-values-list.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DictValueFormModule } from 'src/app/features/manage-dict-values/_features/dict-value-form/dict-value-form.module';

@NgModule({
  declarations: [ManageDictValuesComponent],
  imports: [
    CommonModule,
    MatTabsModule,
    DictValuesListModule,
    MatProgressSpinnerModule,
    DictValueFormModule,
  ],
  exports: [ManageDictValuesComponent],
})
export class ManageDictValuesModule {}
