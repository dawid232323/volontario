import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { isNil } from 'lodash';

export enum SuccessInfoCardButtonEnum {
  Primary,
  Secondary,
}
@Component({
  selector: 'app-success-info-card',
  templateUrl: './success-info-card.component.html',
  styleUrls: ['./success-info-card.component.scss'],
})
export class SuccessInfoCardComponent implements OnInit {
  @Input() titleMessage: string = '';
  @Input() cardContent: string = '';
  @Input() buttonText: string = '';
  @Input() secondaryButtonText?: string;

  @Output() cardButtonClicked = new EventEmitter<SuccessInfoCardButtonEnum>();

  constructor() {}

  ngOnInit(): void {}

  protected readonly SuccessInfoCardButtonEnum = SuccessInfoCardButtonEnum;
  protected readonly isNil = isNil;
}
