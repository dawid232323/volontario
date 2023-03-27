import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CoreButtonInterface } from 'src/app/core/interface/coreButton.interface';

@Component({
  selector: 'app-primary-button',
  templateUrl: './primary-button.component.html',
  styleUrls: ['./primary-button.component.scss'],
})
export class PrimaryButtonComponent implements OnInit, CoreButtonInterface {
  @Input() isLoading = false;
  @Input() buttonType = 'submit';
  @Input() shouldBeEnabled = true;
  @Input() buttonLabel = '';

  @Output() buttonClicked = new EventEmitter<void>();

  constructor() {}

  ngOnInit(): void {}

  onButtonClicked() {
    this.buttonClicked.emit();
  }
}
