import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigurationService } from 'src/app/core/service/configuration.service';
import { Router } from '@angular/router';
import { DefaultAngularEditorConfigProvider } from 'src/app/utils/angular-editor.const';
import { AngularEditorConfig } from '@kolkov/angular-editor';

@Component({
  selector: 'app-regulations-editor',
  templateUrl: './regulations-editor.component.html',
  styleUrls: ['./regulations-editor.component.scss']
})
export class RegulationsEditorComponent implements OnInit {

  regulationsForm: FormGroup = new FormGroup({});

  private _editorConfigProvider: DefaultAngularEditorConfigProvider;

  constructor(private formBuilder: FormBuilder, private confService: ConfigurationService, private router: Router) {
    this._editorConfigProvider = new DefaultAngularEditorConfigProvider();
  }

  public ngOnInit(): void {
    this.initForm();
    this.downloadData();
  }

  public onFormSubmit() {
    if (this.regulationsForm.invalid) {
      return;
    }
    const {useRegulations, rodo} = this.regulationsForm.value;
    this.confService.saveRegulations({useRegulation: useRegulations, rodoRegulation: rodo}).subscribe(() => {
      this.router.navigate(['/', '/home']);
    });
  }

  private initForm() {
    this.regulationsForm = this.formBuilder.group({
      useRegulations: [null, [Validators.required, Validators.minLength(10)]],
      rodo: [null, [Validators.required, Validators.minLength(10)]]
    });
  }

  private downloadData() {
    this.confService.getRegulationsData().subscribe(regulations => {
      this.regulationsForm.patchValue({useRegulations: regulations.useRegulation, rodo: regulations.rodoRegulation});
    })
  }

  public get editorConfig(): AngularEditorConfig {
    return this._editorConfigProvider.config;
  }
}
