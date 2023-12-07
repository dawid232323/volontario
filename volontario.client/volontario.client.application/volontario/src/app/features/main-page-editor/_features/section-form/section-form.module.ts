import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SectionFormComponent } from 'src/app/features/main-page-editor/_features/section-form/section-form.component';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { TileFormTableModule } from 'src/app/features/main-page-editor/_features/tile-form-table/tile-form-table.module';

@NgModule({
  declarations: [SectionFormComponent],
  imports: [
    CommonModule,
    MatInputModule,
    FormsModule,
    MatSelectModule,
    AngularEditorModule,
    ReactiveFormsModule,
    TileFormTableModule,
  ],
  exports: [SectionFormComponent],
})
export class SectionFormModule {}
