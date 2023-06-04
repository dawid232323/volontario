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
import {
  AdvertisementFilterIf,
  AdvertisementVisibilityEnum,
} from 'src/app/core/service/advertisement.service';
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

  public selectedCategories = [];
  public selectedTypes = [];
  public selectedStartDate: Date | null = null;
  public selectedEndDate: Date | null = null;
  public typedTitleSearch: string | null = null;
  public selectedVisibility: AdvertisementVisibilityEnum =
    AdvertisementVisibilityEnum.All;

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

  public onSearchClicked() {
    const searchFilterParams: AdvertisementFilterIf = {
      institutionId: this.loggedUser!.institution!.id!,
    };
    if (this.selectedTab === AdvertisementPanelTabEnum.Assigned) {
      searchFilterParams.contactPersonId = this.loggedUser?.id;
    }
    if (this.selectedTypes.length > 0) {
      searchFilterParams.typeIds = [...this.selectedTypes.values()];
    }
    if (this.selectedCategories.length > 0) {
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
    searchFilterParams.visibility = this.selectedVisibility;
    this.searchTriggered.emit(searchFilterParams);
  }

  public onSearchTitleClear() {
    this.typedTitleSearch = null;
    this.onSearchClicked();
  }

  public clearPanel() {
    this.selectedEndDate = null;
    this.selectedStartDate = null;
    this.typedTitleSearch = null;
    this.selectedCategories = [];
    this.selectedTypes = [];
    this.selectedVisibility = AdvertisementVisibilityEnum.All;
  }

  public get filterHasValue(): boolean {
    if (!isNil(this.selectedEndDate)) {
      return true;
    }
    if (!isNil(this.selectedStartDate)) {
      return true;
    }
    if (!isNil(this.typedTitleSearch) && this.typedTitleSearch !== '') {
      return true;
    }
    if (this.selectedCategories.length > 0) {
      return true;
    }
    if (this.selectedTypes.length > 0) {
      return true;
    }
    if (this.selectedVisibility !== AdvertisementVisibilityEnum.All) {
      return true;
    }
    return false;
  }

  protected readonly AdvertisementVisibilityEnum = AdvertisementVisibilityEnum;
}
