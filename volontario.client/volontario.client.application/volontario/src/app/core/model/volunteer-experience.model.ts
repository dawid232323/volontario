import { DictionaryValueInterface } from 'src/app/core/interface/dictionary-value.interface';

export class VolunteerExperience implements DictionaryValueInterface {
  public description?: string;

  constructor(
    public id: number,
    public name: string,
    public isUsed: boolean,
    public definition?: string
  ) {
    this.description = definition;
  }

  public static fromPayload(payload?: any): VolunteerExperience {
    return new VolunteerExperience(
      payload?.id,
      payload?.name,
      payload?.used,
      payload?.definition
    );
  }
}
