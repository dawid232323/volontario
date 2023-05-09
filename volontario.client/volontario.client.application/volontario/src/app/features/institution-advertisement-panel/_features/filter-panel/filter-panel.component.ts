import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { AdvertisementType } from 'src/app/core/model/advertisement.model';
import { User } from 'src/app/core/model/user.model';
import { AdvertisementFilterIf } from 'src/app/core/service/advertisement.service';
import { isNil } from 'lodash';
import { AdvertisementPanelTabEnum } from 'src/app/features/institution-advertisement-panel/institution-advertisement-panel.component';
import { Subscription } from 'rxjs';
import * as moment from 'moment';

@Component({
  selector: 'app-filter-panel',
  templateUrl: './filter-panel.component.html',
  styleUrls: ['./filter-panel.component.scss'],
})
export class FilterPanelComponent implements OnInit, OnDestroy {
  @Input() selectedTab: AdvertisementPanelTabEnum =
    AdvertisementPanelTabEnum.Assigned;
  @Input() interestCategories: InterestCategoryDTO[] = [];
  @Input() advertisementType: AdvertisementType[] = [];
  @Input() loggedUser?: User = undefined;
  @Input() panelClearEvent!: EventEmitter<void>;

  @Output() searchTriggered = new EventEmitter<AdvertisementFilterIf>();

  private selectedCategories = new Set<number>();
  private selectedTypes = new Set<number>();
  private selectedStartDate: Date | null = null;
  private selectedEndDate: Date | null = null;
  public typedTitleSearch: string | null = null;

  private subscriptions = new Subscription();
  constructor() {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.panelClearEvent.subscribe(this.clearPanel.bind(this))
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  public isCategorySelected(categoryId: number): boolean {
    return this.selectedCategories.has(categoryId);
  }

  public isTypeSelected(typeId: number): boolean {
    return this.selectedTypes.has(typeId);
  }

  public onSearchClicked() {
    const searchFilterParams: AdvertisementFilterIf = {
      institutionId: this.loggedUser!.institution!.id!,
    };
    if (this.selectedTab === AdvertisementPanelTabEnum.Assigned) {
      searchFilterParams.contactPersonId = this.loggedUser?.id;
    }
    if (this.selectedTypes.size > 0) {
      searchFilterParams.typeIds = [...this.selectedTypes.values()];
    }
    if (this.selectedCategories.size > 0) {
      searchFilterParams.interestCategoryIds = [
        ...this.selectedCategories.values(),
      ];
    }
    if (!isNil(this.selectedStartDate)) {
      searchFilterParams.startDate = moment(this.selectedStartDate).format(
        'YYYY-MM-DD'
      );
    }
    if (!isNil(this.selectedEndDate)) {
      searchFilterParams.endDate = moment(this.selectedEndDate).format(
        'YYYY-MM-DD'
      );
    }
    if (!isNil(this.typedTitleSearch)) {
      searchFilterParams.title = this.typedTitleSearch;
    }
    this.searchTriggered.emit(searchFilterParams);
  }

  public onSearchTitleClear() {
    this.typedTitleSearch = null;
    this.onSearchClicked();
  }

  public onStartDateSearchChange(event: any) {
    this.selectedStartDate = event.value;
  }

  public onEndDateSearchChange(event: any) {
    this.selectedEndDate = event.value;
  }

  public onCategorySelect(event: any, selectedId: number) {
    event.stopPropagation();
    event.preventDefault();
    this.onIdSelected(selectedId, this.selectedCategories);
  }

  public onTypeSelect(event: any, selectedId: number) {
    event.stopPropagation();
    event.preventDefault();
    this.onIdSelected(selectedId, this.selectedTypes);
  }

  private onIdSelected(selectedId: number, targetSet: Set<number>) {
    if (targetSet.has(selectedId)) {
      targetSet.delete(selectedId);
    } else {
      targetSet.add(selectedId);
    }
  }

  public get selectedCategoriesSize(): string {
    return this.getSelectionSize(this.selectedCategories);
  }

  public get selectedTypesSize(): string {
    return this.getSelectionSize(this.selectedTypes);
  }

  private getSelectionSize(targetSet: Set<number>): string {
    if (targetSet.size === 0) {
      return '';
    }
    return `(${targetSet.size})`;
  }

  private clearPanel() {
    this.selectedEndDate = null;
    this.selectedStartDate = null;
    this.typedTitleSearch = null;
    this.selectedCategories.clear();
    this.selectedTypes.clear();
  }
}
