import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { UploadProfilePictureModalComponent } from './upload-profile-picture-modal.component';
import { MatDialogModule } from '@angular/material/dialog';
import { FileUploaderModule } from 'src/app/shared/features/file-uplodaer/file-uploader.module';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [UploadProfilePictureModalComponent],
  imports: [
    CommonModule,
    MatDialogModule,
    NgOptimizedImage,
    FileUploaderModule,
    MatButtonModule,
  ],
  exports: [UploadProfilePictureModalComponent],
})
export class UploadProfilePictureModalModule {}
