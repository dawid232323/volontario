import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { AdvertisementBenefit } from 'src/app/core/model/advertisement.model';
import { FormGroup, Validators } from '@angular/forms';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-advertisement-optional-info',
  templateUrl: './advertisement-optional-info.component.html',
  styleUrls: ['./advertisement-optional-info.component.scss'],
})
export class AdvertisementOptionalInfoComponent implements OnInit, OnDestroy {
  @Input() benefits: AdvertisementBenefit[] = [];
  @Input() optionalInfoFormGroup = new FormGroup<any>({});
  @Input() canSubmitForm: boolean = false;
  @Output() formSubmitEvent = new EventEmitter<any>();

  private subscriptions = new Subscription();

  constructor() {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.optionalInfoFormGroup.controls[
        'isPoznanOnly'
      ].valueChanges.subscribe(this.onIsPoznanOnlyChange.bind(this))
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  public onIsPoznanOnlyChange(toggleChange: boolean) {
    if (toggleChange) {
      this.optionalInfoFormGroup.controls['eventPlace'].disable({
        onlySelf: true,
      });
      this.optionalInfoFormGroup.controls['eventPlace'].removeValidators([
        Validators.required,
      ]);
    } else {
      this.optionalInfoFormGroup.controls['eventPlace'].enable({
        onlySelf: true,
      });
      this.optionalInfoFormGroup.controls['eventPlace'].addValidators([
        Validators.required,
      ]);
    }
  }
}
