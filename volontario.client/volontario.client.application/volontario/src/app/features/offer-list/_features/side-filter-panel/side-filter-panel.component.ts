import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InterestCategoryDTO } from 'src/app/core/model/interestCategory.model';
import { AdvertisementType } from 'src/app/core/model/advertisement.model';
import { isNil } from 'lodash';

@Component({
  selector: 'app-side-filter-panel',
  templateUrl: './side-filter-panel.component.html',
  styleUrls: ['./side-filter-panel.component.scss', '../../../../shared/styles/material-styles.scss'],
})
export class SideFilterPanelComponent implements OnInit {
  @Input() interestCategories: InterestCategoryDTO[] = [];
  @Input() offerTypes: AdvertisementType[] = [];
  @Input() selectedCategories?: Set<number>;
  @Input() selectedTypes?: Set<number>;

  @Output() searchTriggered = new EventEmitter<void>();
  @Output() selectionChanged = new EventEmitter<void>();

  constructor() {}

  ngOnInit() {}

  public isCategorySelected(category: InterestCategoryDTO): boolean {
    return this.isItemSelected(category.id, this.selectedCategories);
  }

  public isTypeSelected(type: AdvertisementType): boolean {
    return this.isItemSelected(type.id, this.selectedTypes);
  }

  private isItemSelected(itemId: number, set?: Set<number>) {
    if (isNil(set)) {
      return false;
    }
    return set.has(itemId);
  }

  public onAddRemoveSelectedCategory(category: InterestCategoryDTO) {
    this.onAddRemoveSelectedItem(category.id, this.selectedCategories);
  }

  public onAddRemoveSelectedType(type: AdvertisementType) {
    this.onAddRemoveSelectedItem(type.id, this.selectedTypes);
  }

  private onAddRemoveSelectedItem(itemId: number, set?: Set<number>) {
    if (this.isItemSelected(itemId, set)) {
      set?.delete(itemId);
    } else {
      set?.add(itemId);
    }

    this.selectionChanged.emit();
  }
}
