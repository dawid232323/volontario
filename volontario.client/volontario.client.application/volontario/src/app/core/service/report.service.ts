import { Injectable } from '@angular/core';
import { VolontarioRestService } from './volontarioRest.service';
import { EndpointUrls } from '../../utils/url.util';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  constructor(private restService: VolontarioRestService) {}

  sendReport(
    reportName: string,
    reportDescription: string,
    filesToUpload: any[]
  ): Observable<any> {
    const urlSuffix: string =
      filesToUpload.length !== 0
        ? EndpointUrls.reportWithAttachment
        : EndpointUrls.report;

    return this.restService.post(
      urlSuffix,
      this.createFormData(reportName, reportDescription, filesToUpload)
    );
  }

  private createFormData(
    reportName: string,
    reportDescription: string,
    filesToUpload: any[]
  ): FormData {
    const form_data = new FormData();

    filesToUpload.forEach(file => {
      form_data.append('attachments', file.file);
    });

    form_data.append('reportName', reportName);
    form_data.append('reportDescription', reportDescription);

    return form_data;
  }
}
