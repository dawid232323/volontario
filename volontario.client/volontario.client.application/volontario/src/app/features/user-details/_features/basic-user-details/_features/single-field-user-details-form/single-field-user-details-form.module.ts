import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { SingleFieldUserDetailsFormComponent } from './single-field-user-details-form.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [SingleFieldUserDetailsFormComponent],
  imports: [
    CommonModule,
    MatDialogModule,
    MatInputModule,
    NgOptimizedImage,
    MatButtonModule,
    AngularEditorModule,
    ReactiveFormsModule,
  ],
  exports: [SingleFieldUserDetailsFormComponent],
})
export class SingleFieldUserDetailsFormModule {}
