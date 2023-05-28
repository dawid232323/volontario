import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-contact-info-form',
  templateUrl: './contact-info-form.component.html',
  styleUrls: ['./contact-info-form.component.scss'],
})
export class ContactInfoFormComponent implements OnInit {
  @Input() contactFormGroup = new FormGroup<any>({});
  @Input() currentOfferId?: number;

  constructor() {}

  ngOnInit(): void {}
}
