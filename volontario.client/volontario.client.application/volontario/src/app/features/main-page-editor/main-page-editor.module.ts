import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainPageEditorComponent } from './main-page-editor.component';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { SectionFormModule } from 'src/app/features/main-page-editor/_features/section-form/section-form.module';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  declarations: [MainPageEditorComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    SectionFormModule,
    MatDividerModule,
  ],
  exports: [MainPageEditorComponent],
})
export class MainPageEditorModule {}
