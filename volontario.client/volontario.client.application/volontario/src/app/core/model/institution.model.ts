import { InstitutionContactPersonModel } from 'src/app/core/model/InstitutionContactPerson.model';

interface InstitutionInterface {
  name?: string;
  contactPerson?: InstitutionContactPersonModel;
  krsNumber?: string;
  headquartersAddress?: string;
  tags?: string[];
  description?: string;
  localization?: string;
}

export class InstitutionModelBuilder implements InstitutionInterface {
  contactPerson: InstitutionContactPersonModel | undefined;
  description: string | undefined;
  headquartersAddress: string | undefined;
  krsNumber: string | undefined;
  localization: string | undefined;
  name: string | undefined;
  tags: string[] | undefined;

  public static builder(): InstitutionModelBuilder {
    return new InstitutionModelBuilder();
  }

  public setContactPerson(
    contactPerson: InstitutionContactPersonModel
  ): InstitutionModelBuilder {
    this.contactPerson = contactPerson;
    return this;
  }

  public setDescription(description: string): InstitutionModelBuilder {
    this.description = description;
    return this;
  }

  public setHeadquartersAddress(address: string): InstitutionModelBuilder {
    this.headquartersAddress = address;
    return this;
  }

  public setKrsNumber(krs: string): InstitutionModelBuilder {
    this.krsNumber = krs;
    return this;
  }

  public setLocalization(localization: string): InstitutionModelBuilder {
    this.localization = localization;
    return this;
  }

  public setName(name: string): InstitutionModelBuilder {
    this.name = name;
    return this;
  }

  public setTags(tags: string[]): InstitutionModelBuilder {
    this.tags = tags;
    return this;
  }

  public build(): InstitutionModel {
    return new InstitutionModel(
      this.name,
      this.contactPerson,
      this.krsNumber,
      this.headquartersAddress,
      this.tags,
      this.description,
      this.localization
    );
  }
}
export class InstitutionModel implements InstitutionInterface {
  constructor(
    public name?: string,
    public contactPerson?: InstitutionContactPersonModel,
    public krsNumber?: string,
    public headquartersAddress?: string,
    public tags?: string[],
    public description?: string,
    public localization?: string
  ) {}

  public static fromPayload(payload?: any): InstitutionModel {
    return new InstitutionModel(
      payload?.name,
      InstitutionContactPersonModel.fromPayload(payload?.contactPerson),
      payload?.krsNumber,
      payload?.headquartersAddress,
      payload?.tags,
      payload?.description,
      payload?.localization
    );
  }
}