import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportIssueComponent } from '../../../features/report-issue/report-issue.component';
import { DragAndDropDirective } from '../drag-and-drop/drag-and-drop.directive';
import { FileUploaderComponent } from './file-uploader.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatRippleModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { DragAndDropModule } from '../drag-and-drop/drag-and-drop.module';

@NgModule({
  declarations: [FileUploaderComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatRippleModule,
    DragAndDropModule,
  ],
  exports: [FileUploaderComponent],
})
export class FileUploaderModule {}
