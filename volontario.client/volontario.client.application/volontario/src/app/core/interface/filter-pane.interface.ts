import { ElementRef, EventEmitter } from '@angular/core';
import { MatButton } from '@angular/material/button';

/**
 * Interface that should be implemented by all components used for handling input filter
 */
export interface FilterPaneInterface<T> {
  /**
   * Event that emits filter params of type T.
   */
  searchSubmitEvent: EventEmitter<T>;

  /**
   * Event that should be emitted when clear filter options is triggered.
   */
  filterClearEvent: EventEmitter<void>;

  /**
   * Button that represents search action.
   */
  searchButton: ElementRef<MatButton> | undefined;

  /**
   * Button that represents clear filter actions.
   */
  clearButton: ElementRef<MatButton> | undefined;

  /**
   * Determines if filters can be cleared and if clear filters button should be shown.
   */
  canClearFilters: boolean;

  /**
   * Method triggered when search button is clicked.
   */
  onSearchSubmit(): void;

  /**
   * Method triggered when clear button is clicked.
   */
  onFilterClear(): void;
}
