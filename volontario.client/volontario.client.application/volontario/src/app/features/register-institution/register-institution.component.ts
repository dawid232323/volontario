import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InstitutionService } from 'src/app/core/service/institution.service';
import { arrayLengthValidator } from 'src/app/utils/validator.utils';
import {
  InfoCardButtonEnum,
  InfoCardTypeEnum,
} from 'src/app/shared/features/success-info-card/info-card.component';
import { Router } from '@angular/router';
import { ConfigurationService } from 'src/app/core/service/configuration.service';
import { MatDialog } from '@angular/material/dialog';
import {
  Regulations,
  RegulationType,
} from 'src/app/core/model/configuration.model';
import {
  RegulationPreviewModalComponent,
  RegulationPreviewModalInitialData,
} from 'src/app/shared/features/regulation-preview-modal/regulation-preview-modal.component';

@Component({
  selector: 'app-register-institution',
  templateUrl: './register-institution.component.html',
  styleUrls: ['./register-institution.component.scss'],
})
export class RegisterInstitutionComponent implements OnInit {
  basicInfoFormGroup: FormGroup = new FormGroup<any>({});
  additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  hasRegisteredCorrectly: boolean = false;
  isPerformingRegistration: boolean = false;

  successTitle = 'Wniosek złożony pomyślnie';
  successContent =
    'Wniosek o rejestrację instytucji został złożony pomyślnie! Sprawdź ' +
    'skrzynkę mailową by ustawić hasło. Po ustawieniu hasła logowanie na założone konto będzie możliwe, natomiast do czasu weryfikacji przez naszych moderatorów wszystkie dodane przez Ciebie ogłoszenia będą niewidoczne.';
  buttonText = 'Strona główna';

  private _regulations?: Regulations;

  constructor(
    private formBuilder: FormBuilder,
    private institutionService: InstitutionService,
    private router: Router,
    private configurationService: ConfigurationService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.initForms();
    this.downloadInitialData();
  }

  public onFormSubmit() {
    if (!this.canSubmitForm) {
      return;
    }
    this.isPerformingRegistration = true;
    const institutionModel =
      this.institutionService.getInstitutionModelFromFormData(
        this.basicInfoFormGroup.value,
        this.additionalInfoFormGroup.value
      );
    this.institutionService.createInstitution(institutionModel).subscribe({
      next: this.onRegisterSuccess.bind(this),
    });
  }

  public onCardButtonClicked($event: InfoCardButtonEnum) {
    if ($event === InfoCardButtonEnum.Primary) {
      this.router.navigate(['/']);
    }
  }

  public onShowUseRegulations() {
    this.openRegulationsDialog(
      RegulationType.Use,
      this._regulations?.useRegulation!
    );
  }

  public onShowRodoRegulations() {
    this.openRegulationsDialog(
      RegulationType.Rodo,
      this._regulations?.rodoRegulation!
    );
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

  private initForms() {
    this.basicInfoFormGroup = this.formBuilder.group({
      institutionName: [null, [Validators.required, Validators.maxLength(50)]],
      krsNumber: [
        null,
        [
          Validators.minLength(10),
          Validators.maxLength(10),
          Validators.pattern('[0-9]*'),
        ],
      ],
      address: [null, [Validators.required]],
      registerPersonName: [null, [Validators.required]],
      registerPersonLastName: [null, [Validators.required]],
      registerPersonEmail: [null, [Validators.required, Validators.email]],
      registerPersonPhoneNumber: [
        null,
        [
          Validators.required,
          Validators.minLength(9),
          Validators.maxLength(9),
          Validators.pattern('[0-9]*'),
        ],
      ],
    });
    this.additionalInfoFormGroup = this.formBuilder.group({
      institutionTags: [[], [Validators.required, arrayLengthValidator(10)]],
      institutionDescription: [
        null,
        [Validators.required, Validators.maxLength(500)],
      ],
      operationPlace: [null, [Validators.required]],
      rulesApprove: [false, [Validators.requiredTrue]],
      rodoApprove: [false, [Validators.requiredTrue]],
    });
  }

  private downloadInitialData() {
    this.configurationService.getRegulationsData().subscribe(regulations => {
      this._regulations = regulations;
    });
  }

  private onRegisterSuccess(result: any) {
    this.isPerformingRegistration = false;
    this.hasRegisteredCorrectly = true;
  }

  get canSubmitForm(): boolean {
    return this.basicInfoFormGroup.valid && this.additionalInfoFormGroup.valid;
  }

  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
}
