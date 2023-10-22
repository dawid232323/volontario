import { Component, OnInit } from '@angular/core';
import { FileUploaderConfiguration } from 'src/app/core/model/common.model';
import { FileTransferResult } from 'src/app/core/model/error.model';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-upload-profile-picture-modal',
  templateUrl: './upload-profile-picture-modal.component.html',
  styleUrls: ['./upload-profile-picture-modal.component.scss'],
})
export class UploadProfilePictureModalComponent {
  public fileUploaderConfig: FileUploaderConfiguration = {
    allow_multi_selection: false,
    accepted_MIME_types: 'image/png, image/jpeg',
  };
  public selectedFiles: { file: File }[] = [];
  public hasMaxSizeError = false;
  public hasWrongExtensionError = false;

  constructor(
    private matDialogRef: MatDialogRef<UploadProfilePictureModalComponent>
  ) {}

  public onModalClose() {
    if (!this.canCloseModal()) {
      return;
    }
    this.matDialogRef.close(this.selectedFiles[0].file);
  }

  public onPictureEditAbort() {
    this.matDialogRef.close(undefined);
  }

  public onFileTransferChange(event: FileTransferResult) {
    switch (event) {
      case FileTransferResult.WRONG_MIME_TYPE:
        this.hasWrongExtensionError = true;
        return;
      case FileTransferResult.MAX_SIZE_EXCEEDED:
        this.hasMaxSizeError = true;
        return;
      case FileTransferResult.OK:
        this.hasMaxSizeError = false;
        this.hasWrongExtensionError = false;
        return;
      default:
        this.hasMaxSizeError = true;
        this.hasWrongExtensionError = true;
        return;
    }
  }

  public canCloseModal() {
    return this.selectedFiles.length === 1;
  }
}
