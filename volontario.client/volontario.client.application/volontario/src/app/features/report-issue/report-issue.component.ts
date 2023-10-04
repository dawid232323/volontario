import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { FileTransferResult } from '../../core/model/error.model';
import { ReportService } from '../../core/service/report.service';
import { VolontarioGlobalErrorHandlerInterceptor } from '../../core/interceptor/VolontarioGlobalErrorHandler.interceptor';

@Component({
  selector: 'app-report-issue',
  templateUrl: 'report-issue.component.html',
  styleUrls: ['report-issue.component.scss'],
})
export class ReportIssueComponent implements OnInit {
  reportIssueForm: FormGroup = new FormGroup<any>({});
  filesToUpload: any[] = [];
  errorMessage: string | null = null;
  reportMade: boolean = false;
  fileUploadInProgress: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private reportService: ReportService,
    private errorHandler: VolontarioGlobalErrorHandlerInterceptor
  ) {}

  ngOnInit(): void {
    this.buildForm();
  }

  buildForm(): void {
    this.reportIssueForm = this.formBuilder.group({
      reportName: [
        null,
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(150),
        ],
      ],
      reportDescription: [
        null,
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(1500),
        ],
      ],
    });
  }

  canFormBeSubmitted(): boolean {
    return this.reportIssueForm.touched && this.reportIssueForm.valid;
  }

  onSubmitForm(): void {
    if (this.canFormBeSubmitted()) {
      this.fileUploadInProgress = true;
      this.reportService
        .sendReport(
          this.reportIssueForm.controls['reportName'].getRawValue(),
          this.reportIssueForm.controls['reportDescription'].getRawValue(),
          this.filesToUpload
        )
        .subscribe({
          error: (err: any) => {
            this.errorHandler.handleError(err);
            this.reportMade = false;
            this.fileUploadInProgress = false;
          },
          complete: () => {
            this.fileUploadInProgress = false;
            this.reportMade = true;
            setTimeout(() => {
              this.router.navigate(['/home']);
            }, 3000);
          },
        });
    }
  }

  setErrorMessage(errorType: FileTransferResult): void {
    switch (errorType) {
      case FileTransferResult.MAX_SIZE_EXCEEDED:
        this.errorMessage = 'Maksymalna wielkość zdjęcia to 2MB';
        break;
      case FileTransferResult.MAX_QUANTITY_EXCEEDED:
        this.errorMessage = 'Można załączyć maksymalnie 5 zdjęć.';
        break;
      case FileTransferResult.WRONG_MIME_TYPE:
        this.errorMessage =
          'Można załączyć tylko pliki w formacie .png, .jpg oraz .gif';
        break;
      case FileTransferResult.OK:
        this.errorMessage = null;
    }
  }
}
