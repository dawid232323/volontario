import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-success-info-card',
  templateUrl: './success-info-card.component.html',
  styleUrls: ['./success-info-card.component.scss'],
})
export class SuccessInfoCardComponent implements OnInit {
  @Input() titleMessage: string = '';
  @Input() cardContent: string = '';
  @Input() buttonText: string = '';

  @Output() cardButtonClicked = new EventEmitter<void>();

  constructor() {}

  ngOnInit(): void {}
}
