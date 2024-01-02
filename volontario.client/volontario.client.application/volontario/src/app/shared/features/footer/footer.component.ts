import { Component, OnInit } from '@angular/core';
import { ConfigurationService } from 'src/app/core/service/configuration.service';
import { MatDialog } from '@angular/material/dialog';
import {
  Regulations,
  RegulationType,
} from 'src/app/core/model/configuration.model';
import { firstValueFrom } from 'rxjs';
import { isNil } from 'lodash';
import {
  RegulationPreviewModalComponent,
  RegulationPreviewModalInitialData,
} from 'src/app/shared/features/regulation-preview-modal/regulation-preview-modal.component';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {
  private _regulations?: Regulations;
  constructor(
    private configurationService: ConfigurationService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {}

  public async onRegulationsClick() {
    await this.initRegulations();
    this.openRegulationsDialog(
      RegulationType.Use,
      this._regulations?.useRegulation!
    );
  }

  public async onRodoClick() {
    await this.initRegulations();
    this.openRegulationsDialog(
      RegulationType.Rodo,
      this._regulations?.rodoRegulation!
    );
  }

  private async initRegulations() {
    if (isNil(this._regulations)) {
      this._regulations = await firstValueFrom(
        this.configurationService.getRegulationsData()
      );
    }
  }

  private openRegulationsDialog(
    regulationType: RegulationType,
    content: string
  ) {
    const initialData: RegulationPreviewModalInitialData = {
      regulationType,
      modalContent: content,
    };
    this.dialog.open(RegulationPreviewModalComponent, { data: initialData });
  }
}
