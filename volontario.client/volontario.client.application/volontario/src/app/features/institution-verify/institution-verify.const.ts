export class InstitutionVerifyConst {
  static readonly acceptTitle: string = 'Instytucja zweryfikowana pomyślnie';
  static readonly acceptContent: string =
    'Do osoby kontaktowej instytucji wysłaliśmy mail z potwierdzeniem wykonanej przez ciebie operacji, a także instrukcją utworzenia konta w systemie';
  static readonly rejectTitle: string = 'Wniosek instytucji odrzucony';
  static readonly rejectContent: string =
    'Do osoby kontaktowej instytucji wysłaliśmy mail z informacją o odrzuceniu wniosku danej instytucji';
}

export enum VerifyType {
  ACCEPT = 'accept',
  REJECT = 'reject',
}
