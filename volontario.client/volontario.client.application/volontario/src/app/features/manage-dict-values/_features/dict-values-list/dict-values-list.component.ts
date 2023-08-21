import {
  AfterViewChecked,
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { isNil } from 'lodash';

export enum DictValueOperationTypeEnum {
  Add,
  Edit,
  Delete,
}

export enum DictionaryValueTypeEnum {
  InterestCategory,
  ExpLevel,
  AddBenefits,
}

export interface DictValueOperationInterface {
  valueType: DictionaryValueTypeEnum;
  operationType: DictValueOperationTypeEnum;
  operationData?: DictionaryValueInterface;
}

@Component({
  selector: 'app-dict-values-list',
  templateUrl: './dict-values-list.component.html',
  styleUrls: ['./dict-values-list.component.scss'],
})
export class DictValuesListComponent implements OnInit, AfterViewInit {
  private _valuesList: DictionaryValueInterface[] = [];
  private _shouldShowDescription = true;
  private _dataSource = new MatTableDataSource<DictionaryValueInterface>();
  private _valueType: DictionaryValueTypeEnum =
    DictionaryValueTypeEnum.InterestCategory;

  @ViewChild(MatSort) sort?: MatSort;
  @ViewChild(MatPaginator) paginator?: MatPaginator;

  @Output() operationEvent = new EventEmitter<DictValueOperationInterface>();

  private _visibleColumns: string[] = [
    'name',
    'description',
    'isActive',
    'action',
  ];
  constructor() {}

  ngOnInit(): void {}

  ngAfterViewInit() {
    this.dataSource.sort = this.sort!;
    this.dataSource.paginator = this.paginator!;
  }

  @Input()
  get valuesList(): DictionaryValueInterface[] {
    return this._valuesList;
  }

  set valuesList(value: DictionaryValueInterface[]) {
    this._valuesList = value;
    this._dataSource.data = this._valuesList;
  }

  @Input()
  get shouldShowDescription(): boolean {
    return this._shouldShowDescription;
  }

  set shouldShowDescription(value: boolean) {
    this._shouldShowDescription = value;
  }

  @Input()
  get valueType(): DictionaryValueTypeEnum {
    return this._valueType;
  }

  set valueType(value: DictionaryValueTypeEnum) {
    this._valueType = value;
  }

  get dataSource(): MatTableDataSource<DictionaryValueInterface> {
    return this._dataSource;
  }

  get visibleColumns(): string[] {
    return this._visibleColumns;
  }

  public onEditValueClick(selectedValue: DictionaryValueInterface) {
    this.operationEvent.emit({
      valueType: this._valueType,
      operationType: DictValueOperationTypeEnum.Edit,
      operationData: selectedValue,
    });
  }

  public onDisableEnableValueClick(selectedValue: DictionaryValueInterface) {
    this.operationEvent.emit({
      valueType: this._valueType,
      operationType: DictValueOperationTypeEnum.Delete,
      operationData: selectedValue,
    });
  }

  public onAddValueClicked() {
    this.operationEvent.emit({
      valueType: this._valueType,
      operationType: DictValueOperationTypeEnum.Add,
    });
  }

  public updateValue(valueToUpdate: DictionaryValueInterface) {
    const foundIndex = this._valuesList.findIndex(
      value => value.id === valueToUpdate.id
    );
    if (foundIndex === -1) {
      this._valuesList.push(valueToUpdate);
    } else {
      this._valuesList[foundIndex] = valueToUpdate;
    }
    this._dataSource.data = this._valuesList;
  }
}
