import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';
import { Observable } from 'rxjs';

export interface DictionaryValuesServiceInterface<
  T extends DictionaryValueInterface
> {
  getUsedValues(): Observable<T[]>;
  getNotUsedValues(): Observable<T[]>;
  getAllValues(): Observable<T[]>;
  createValue(body: T): Observable<T>;
  updateValue(body: T, valueId: number): Observable<T>;
  activateDeactivateValue(
    shouldBeDeactivated: boolean,
    valueId: number
  ): Observable<void>;
}
