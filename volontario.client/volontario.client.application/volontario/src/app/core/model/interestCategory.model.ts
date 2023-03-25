export class InterestCategoryDTO {
  constructor(
    public id: number,
    public name: string,
    public description: string
  ) {}

  public static fromPayload(payload: any): InterestCategoryDTO {
    return new InterestCategoryDTO(
      payload?.id,
      payload?.name,
      payload?.description
    );
  }
}
