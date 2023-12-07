export enum SectionType {
  WithTiles = 'Sekcja z krokami do wykonania',
  TextOnly = 'Sekcja z tekstem i obrazkiem',
}

export enum ImageCategories {
  Default = 'Default',
  Collaboration = 'Collaboration',
}

export type ConfigurationImage = {
  type: ImageCategories;
  path: string;
  name: string;
};

export const ConfigurationImages: ConfigurationImage[] = [
  {
    type: ImageCategories.Default,
    path: 'assets/section_1.svg',
    name: 'Podstawowe',
  },
  {
    type: ImageCategories.Collaboration,
    path: 'assets/section_2.svg',
    name: 'Współpraca',
  },
];

export class LandingPageTile {
  constructor(
    public title: string | null,
    public stepContent: string | null,
    public stepIcon: string | null
  ) {}
}

export class LandingPageSection {
  constructor(
    public id: string | null,
    public title: string | null,
    public type: SectionType,
    public content: string | null,
    public imageCategory: ImageCategories | null,
    public tiles: LandingPageTile[]
  ) {}
}

export class LandingPageDto {
  constructor(public sections: LandingPageSection[]) {}
}
