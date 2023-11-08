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

  constructor(
    private authService: SecurityService,
    private interestCategoryService: InterestCategoryService,
    private experienceService: VolunteerExperienceService,
    public router: Router,
    private errorDialogService: ErrorDialogService
  ) {}

  ngOnInit(): void {
    forkJoin([
      this.interestCategoryService.getPublicValues(),
      this.experienceService.getPublicValues(),
    ]).subscribe(([categories, experiences]) => {
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

  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
