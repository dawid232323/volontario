import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { countCharacters } from '../../../../utils/validator.utils';

@Component({
  selector: 'app-register-basic-info-form',
  templateUrl: './register-basic-info-form.component.html',
  styleUrls: ['./register-basic-info-form.component.scss'],
})
export class RegisterBasicInfoFormComponent implements OnInit {
  @Input() basicInfoFormGroup: FormGroup = new FormGroup<any>({});

  constructor() {}

  ngOnInit(): void {}

  protected readonly countCharacters = countCharacters;
}
