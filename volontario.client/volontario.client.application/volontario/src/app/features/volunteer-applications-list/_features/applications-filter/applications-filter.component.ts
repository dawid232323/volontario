import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { cloneDeep } from 'lodash';
import {
  ApplicationStateIf,
  ApplicationStates,
} from 'src/app/core/model/application.model';

@Component({
  selector: 'app-applications-filter',
  templateUrl: './applications-filter.component.html',
  styleUrls: ['./applications-filter.component.scss'],
})
export class ApplicationsFilterComponent implements OnInit {
  @Input() title?: string;
  @Output() titleChange = new EventEmitter<string | undefined>();

  @Input() startDate?: Date;
  @Output() startDateChange = new EventEmitter<Date | undefined>();

  @Input() endDate?: Date;
  @Output() endDateChange = new EventEmitter<Date | undefined>();

  @Input() applicationState?: string;
  @Output() applicationStateChange = new EventEmitter<string | undefined>();

  @Output() searchEvent = new EventEmitter<void>();

  private _applicationStates: ApplicationStateIf[] = [];
  constructor() {}

  ngOnInit(): void {
    this.prepareApplicationStatesData();
  }

  get applicationStates(): ApplicationStateIf[] {
    return this._applicationStates;
  }

  private prepareApplicationStatesData() {
    this._applicationStates = cloneDeep(ApplicationStates.allStates);
  }

  onSearchButtonClicked() {
    this.searchEvent.emit();
  }
}
