import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { FileUploaderConfiguration } from 'src/app/core/model/common.model';
import { FileTransferResult } from '../../../core/model/error.model';

@Component({
  selector: 'app-file-uploader',
  templateUrl: './file-uploader.component.html',
  styleUrls: ['./file-uploader.component.scss'],
})
export class FileUploaderComponent implements OnInit, OnDestroy {
  @Input() config!: FileUploaderConfiguration;

  @Input()
  selectedFiles: {
    file: File;
  }[] = [];

  @Output()
  fileTransferResultEmitter: EventEmitter<FileTransferResult> =
    new EventEmitter<FileTransferResult>();

  @ViewChild('fileSelector', { static: false }) file_selector!: ElementRef;

  fileSelectionForm: FormGroup;

  private subscriptions!: Subscription;

  constructor() {
    this.fileSelectionForm = new FormGroup({
      file_selection: new FormControl(),
    });
  }

  ngOnInit(): void {
    this.trackFileSelection();
  }

  trackFileSelection(): void {
    this.subscriptions = new Subscription();

    this.subscriptions.add(
      this.fileSelectionForm
        .get('file_selection')
        ?.valueChanges.subscribe(() => {
          const file_selection = this.file_selector.nativeElement;
          this.selectFiles(file_selection.files);
          this.file_selector.nativeElement.value = '';
        })
    );
  }

  selectFiles(sent_files: FileList): void {
    if (sent_files.length + this.selectedFiles.length > 5) {
      this.fileTransferResultEmitter.emit(
        FileTransferResult.MAX_QUANTITY_EXCEEDED
      );
      return;
    }
    const correctFiles: {
      file: any;
    }[] = [];

    for (let i = 0; i < sent_files.length; i++) {
      const sentFile = <File>sent_files.item(i);
      if (!(this.config.accepted_MIME_types.indexOf(sentFile.type) >= 0)) {
        this.fileTransferResultEmitter.emit(FileTransferResult.WRONG_MIME_TYPE);
        return;
      } else if (sentFile.size > 2000000) {
        // maximum is 2MB per file.
        this.fileTransferResultEmitter.emit(
          FileTransferResult.MAX_SIZE_EXCEEDED
        );
        return;
      } else {
        let selected_file = {
          file: sentFile,
        };
        correctFiles.push(selected_file);
      }
    }

    for (let correct_file of correctFiles) {
      this.selectedFiles.push(correct_file);
    }
    this.fileTransferResultEmitter.emit(FileTransferResult.OK);
  }

  unselectFile(index: number) {
    this.selectedFiles.splice(index, 1);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  public canSelectAnotherFile() {
    if (this.config.allow_multi_selection) {
      return this.selectedFiles.length < 5;
    }
    return this.selectedFiles.length < 1;
  }
}
