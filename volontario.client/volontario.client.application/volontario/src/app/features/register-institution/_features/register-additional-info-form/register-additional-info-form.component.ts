import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { MatChipInputEvent } from '@angular/material/chips';

@Component({
  selector: 'app-register-additional-info-form',
  templateUrl: './register-additional-info-form.component.html',
  styleUrls: ['./register-additional-info-form.component.scss'],
})
export class RegisterAdditionalInfoFormComponent implements OnInit {
  @Input() additionalInfoFormGroup: FormGroup = new FormGroup<any>({});
  @Input() canSubmit: boolean = false;

  @Output() formSubmit = new EventEmitter<void>();

  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  constructor() {}

  ngOnInit(): void {}

  get institutionTags() {
    return this.additionalInfoFormGroup.get('institutionTags');
  }

  addTag(event: MatChipInputEvent) {
    const value = event.value;
    const input = event.input;
    if ((value || '').trim()) {
      this.institutionTags?.setValue([
        ...this.institutionTags?.value,
        value.trim(),
      ]);
      this.institutionTags?.updateValueAndValidity();
      if (input) {
        input.value = '';
      }
    }
  }

  removeTag(tag: string) {
    const tagIndex = this.institutionTags?.value.indexOf(tag);

    if (tagIndex >= 0) {
      this.institutionTags?.value.splice(tagIndex, 1);
      this.institutionTags?.updateValueAndValidity();
    }
  }
}
