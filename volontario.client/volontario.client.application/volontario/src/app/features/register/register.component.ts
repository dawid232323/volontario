import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VolunteerRegisterDTO } from 'src/app/core/model/volunteer.model';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { SelectFieldModelIf } from 'src/app/core/interface/selectField.interface';
import { forkJoin } from 'rxjs';
import { VolunteerExperienceService } from 'src/app/core/service/volunteer-experience.service';
import { InfoCardTypeEnum } from '../../shared/features/success-info-card/info-card.component';
import { ErrorDialogService } from 'src/app/core/service/error-dialog.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfigurationService } from 'src/app/core/service/configuration.service';
import {
  Regulations,
  RegulationType,
} from 'src/app/core/model/configuration.model';
import { MatDialog } from '@angular/material/dialog';
import {
  RegulationPreviewModalComponent,
  RegulationPreviewModalInitialData,
} from 'src/app/shared/features/regulation-preview-modal/regulation-preview-modal.component';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  isPerformingRegistration: boolean = false;
  hasRegisteredCorrectly = false;
  interestCategories: SelectFieldModelIf[] = [];
  experienceLevels: SelectFieldModelIf[] = [];

  successMessageTitle = 'Rejestracja przebiegła pomyślnie';
  successMessageContent =
    'Na twój email uczelniany wysłaliśmy link do weryfikacji konta. Wejdź w\n' +
    '        niego aby zweryfikować swoje konto i zacząć korzystać z Volontario';
  successButtonText = 'Ekran logowania';

  private regulations?: Regulations;

  constructor(
    private authService: SecurityService,
    private interestCategoryService: InterestCategoryService,
    private experienceService: VolunteerExperienceService,
    public router: Router,
    private errorDialogService: ErrorDialogService,
    private configurationService: ConfigurationService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    forkJoin([
      this.interestCategoryService.getPublicValues(),
      this.experienceService.getPublicValues(),
      this.configurationService.getRegulationsData(),
    ]).subscribe(([categories, experiences, regulations]) => {
      this.regulations = regulations;
      this.interestCategories = categories.map(category => {
        return {
          value: category.id,
          viewValue: category.name,
        };
      });
      this.experienceLevels = experiences.map(expLevel => {
        return {
          value: expLevel.id,
          viewValue: expLevel.name,
          helpValue: expLevel.description,
        };
      });
    });
  }

  onRegisterFormSubmit(event: VolunteerRegisterDTO) {
    this.isPerformingRegistration = true;
    this.authService.registerVolunteer(event).subscribe({
      next: () => {
        this.isPerformingRegistration = false;
        this.hasRegisteredCorrectly = true;
      },
      error: this.handleRegisterError.bind(this),
    });
  }

  public onShowUseRegulations() {
    this.openRegulationsDialog(
      RegulationType.Use,
      this.regulations?.useRegulation!
    );
  }

  public onShowRodoRegulations() {
    this.openRegulationsDialog(
      RegulationType.Rodo,
      this.regulations?.rodoRegulation!
    );
  }

  private handleRegisterError(error: HttpErrorResponse) {
    this.isPerformingRegistration = false;
    const dialogTitle =
      error.status === 400 ? 'Niepoprawne dane do rejestracji' : undefined;
    const dialogMessage =
      error.status === 400
        ? 'Sprawdź treść błędu powyżej i popraw niepoprawne dane'
        : undefined;
    this.errorDialogService.openDefaultErrorDialog({
      error: error,
      dialogTitle: dialogTitle,
      dialogMessage: dialogMessage,
    });
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

  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
