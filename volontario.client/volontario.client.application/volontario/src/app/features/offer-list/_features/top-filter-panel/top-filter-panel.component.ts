import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-top-filter-panel',
  templateUrl: './top-filter-panel.component.html',
  styleUrls: ['./top-filter-panel.component.scss'],
})
export class TopFilterPanelComponent implements OnInit {
  @Input() offerName?: string;
  @Output() offerNameChange: EventEmitter<string> = new EventEmitter<string>();

  @Input() offerStartDate?: Date;
  @Output() offerStartDateChange: EventEmitter<Date> = new EventEmitter<Date>();

  @Input() offerEndDate?: Date;
  @Output() offerEndDateChange: EventEmitter<Date> = new EventEmitter<Date>();

  @Input() hasActiveFilters: boolean = false;

  @Output() searchClicked = new EventEmitter<void>();
  @Output() filtersCleared = new EventEmitter<void>();

  constructor() {}

  ngOnInit(): void {}

  onSearchClicked() {
    this.searchClicked.emit();
  }

  public onOfferNameChange(newValue: string) {
    this.offerName = newValue;
    this.offerNameChange.emit(this.offerName);
  }

  public onStartDateChange(value: Date) {
    this.offerStartDate = value;
    this.offerStartDateChange.emit(this.offerStartDate);
  }

  public onEndDateChange(value: Date) {
    this.offerEndDate = value;
    this.offerEndDateChange.emit(this.offerEndDate);
  }
}
