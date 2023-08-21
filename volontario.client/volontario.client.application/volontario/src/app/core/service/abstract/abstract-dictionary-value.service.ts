import { DictionaryValuesServiceInterface } from 'src/app/core/interface/dictionary-values-service.interface';
import { forkJoin, map, Observable } from 'rxjs';
import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';
import { VolontarioRestService } from 'src/app/core/service/volontarioRest.service';

export abstract class AbstractDictionaryValueService<
  T extends DictionaryValueInterface
> implements DictionaryValuesServiceInterface<DictionaryValueInterface>
{
  protected constructor(protected restService: VolontarioRestService) {}

  public abstract getUsedValues(): Observable<DictionaryValueInterface[]>;
  public abstract getNotUsedValues(): Observable<DictionaryValueInterface[]>;
  protected abstract deactivateValue(valueId: number): Observable<void>;

  protected abstract activateValue(valueId: number): Observable<void>;
  protected abstract getCreateUpdateUrl(valueId?: number): string;

  public createValue(body: DictionaryValueInterface): Observable<T> {
    return this.restService.post(this.getCreateUpdateUrl(), body);
  }

  public updateValue(
    body: T,
    valueId: number
  ): Observable<DictionaryValueInterface> {
    return this.restService.put(this.getCreateUpdateUrl(valueId), body);
  }

  public getAllValues(): Observable<DictionaryValueInterface[]> {
    return forkJoin([this.getUsedValues(), this.getNotUsedValues()]).pipe(
      map(([usedValues, notUsedValues]) => {
        return [...usedValues, ...notUsedValues];
      })
    );
  }

  public activateDeactivateValue(
    shouldBeDeactivated: boolean,
    valueId: number
  ): Observable<void> {
    if (shouldBeDeactivated) {
      return this.deactivateValue(valueId);
    }
    return this.activateValue(valueId);
  }
}
