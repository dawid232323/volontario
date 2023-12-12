import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegulationsEditorComponent } from './regulations-editor.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [RegulationsEditorComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    AngularEditorModule,
    MatButtonModule,
  ],
  exports: [RegulationsEditorComponent],
})
export class RegulationsEditorModule {}
