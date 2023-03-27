import { EventEmitter } from '@angular/core';

export interface CoreButtonInterface {
  isLoading: boolean;
  buttonType: string;
  shouldBeEnabled: boolean;
  buttonLabel: string;
  buttonClicked: EventEmitter<void>;
  buttonAccentPrimaryColor?: string;
  buttonAccentHoverColor?: string;
  buttonAccentDisabledColor?: string;
}
