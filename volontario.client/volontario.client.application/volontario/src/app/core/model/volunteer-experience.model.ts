export class VolunteerExperience {
  constructor(
    public id: number,
    public name: string,
    public definition: string
  ) {}

  public static fromPayload(payload?: any): VolunteerExperience {
    return new VolunteerExperience(
      payload?.id,
      payload?.name,
      payload?.definition
    );
  }
}
