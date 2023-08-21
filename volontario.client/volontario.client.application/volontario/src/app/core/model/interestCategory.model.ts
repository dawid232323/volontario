import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';

export class InterestCategoryDTO implements DictionaryValueInterface {
  constructor(
    public id: number,
    public name: string,
    public isUsed: boolean,
    public description?: string
  ) {}

  public static fromPayload(payload: any): InterestCategoryDTO {
    return new InterestCategoryDTO(
      payload?.id,
      payload?.name,
      payload?.used,
      payload?.description
    );
  }
}
