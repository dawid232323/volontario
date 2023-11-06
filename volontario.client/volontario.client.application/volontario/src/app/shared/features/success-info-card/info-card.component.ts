import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { isNil } from 'lodash';

export enum InfoCardTypeEnum {
  Success,
  Error,
}

export enum InfoCardButtonEnum {
  Primary,
  Secondary,
}

@Component({
  selector: 'app-info-card',
  templateUrl: './info-card.component.html',
  styleUrls: ['./info-card.component.scss'],
})
export class InfoCardComponent implements OnInit {
  @Input() titleMessage: string = '';
  @Input() cardContent: string = '';
  @Input() buttonText: string = '';
  @Input() secondaryButtonText?: string;
  @Input() infoType: InfoCardTypeEnum = InfoCardTypeEnum.Success;

  @Output() cardButtonClicked = new EventEmitter<InfoCardButtonEnum>();

  constructor() {}

  ngOnInit(): void {}

  protected readonly InfoCardButtonEnum = InfoCardButtonEnum;
  protected readonly InfoCardTypeEnum = InfoCardTypeEnum;
  protected readonly isNil = isNil;
}
