import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VolunteerRegisterDTO } from 'src/app/core/model/volunteer.model';
import { SecurityService } from 'src/app/core/service/security/security.service';
import { InterestCategoryService } from 'src/app/core/service/interestCategory.service';
import { SelectFieldModelIf } from 'src/app/core/interface/selectField.interface';
import { forkJoin } from 'rxjs';
import { VolunteerExperienceService } from 'src/app/core/service/volunteer-experience.service';
import { InfoCardTypeEnum } from '../../shared/features/success-info-card/info-card.component';

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
    public router: Router
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

  private handleRegisterError(error: any) {
    this.isPerformingRegistration = false;
    console.error(error);
  }

  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
