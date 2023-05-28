import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-reason-form',
  templateUrl: './reason-form.component.html',
  styleUrls: ['./reason-form.component.scss'],
})
export class ReasonFormComponent implements OnInit {
  @Input() reasonFormGroup = new FormGroup<any>({});
  @Input() canSubmitForm = false;
  @Input() currentOfferId?: number;
  @Output() formSubmit = new EventEmitter<void>();

  constructor() {}

  ngOnInit(): void {}
}
