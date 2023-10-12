export enum SingleUserDetailsFormUsage {
  CREATE,
  EDIT,
}

export interface SingleUserDetailsConfigProviderIf {
  title: string;
  usage: SingleUserDetailsFormUsage;
  initialData: string | undefined;
  setData(value: string): void;
}

export class UserInterestsConfigProvider
  implements SingleUserDetailsConfigProviderIf
{
  private readonly _usage: SingleUserDetailsFormUsage;
  private readonly _title: string;
  constructor(
    usage: SingleUserDetailsFormUsage,
    private _initialData?: string
  ) {
    this._usage = usage;
    if (this._usage === SingleUserDetailsFormUsage.CREATE) {
      this._title = 'Dodaj zainteresowania';
    } else {
      this._title = 'Edytuj zainteresowania';
    }
  }

  setData(value: string) {
    this._initialData = value;
  }

  get title(): string {
    return this._title;
  }

  get usage(): SingleUserDetailsFormUsage {
    return this._usage;
  }

  get initialData(): string | undefined {
    return this._initialData;
  }
}

export class UserExperienceDescriptionConfigProvider
  implements SingleUserDetailsConfigProviderIf
{
  private readonly _usage: SingleUserDetailsFormUsage;
  private readonly _title: string;
  constructor(
    usage: SingleUserDetailsFormUsage,
    private _initialData?: string
  ) {
    this._usage = usage;
    if (this._usage === SingleUserDetailsFormUsage.CREATE) {
      this._title = 'Dodaj opis doświadczenia';
    } else {
      this._title = 'Edytuj opis doświadczenia';
    }
  }

  setData(value: string) {
    this._initialData = value;
  }

  get title(): string {
    return this._title;
  }

  get usage(): SingleUserDetailsFormUsage {
    return this._usage;
  }

  get initialData(): string | undefined {
    return this._initialData;
  }
}
