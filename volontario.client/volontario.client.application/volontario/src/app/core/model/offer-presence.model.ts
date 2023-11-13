export enum PresenceStateEnum {
  Unresolved = 'Nierozstrzygnięta',
  Confirmed = 'Potwierdzona',
  Denied = 'Zaprzeczona',
}

export class VolunteerPresenceDto {
  constructor(
    public volunteerId: number,
    public presenceState: PresenceStateEnum | 'CONFIRMED' | 'DENIED'
  ) {}
}

export class InstitutionVoluntaryPresenceModel {
  constructor(
    public wasPresenceConfirmed: boolean,
    public canDecisionBeChanged: boolean,
    public decisionChangeDeadlineDate: Date,
    public canPostponeReminder: boolean
  ) {}

  public static fromPayload(payload?: any): InstitutionVoluntaryPresenceModel {
    return new InstitutionVoluntaryPresenceModel(
      payload?.wasPresenceConfirmed,
      payload?.canDecisionBeChanged,
      new Date(payload?.decisionChangeDeadlineDate),
      payload?.canPostponeReminder
    );
  }
}

export class VolunteerPresenceModel {
  constructor(
    public confirmationState: PresenceStateEnum,
    public canDecisionBeChanged: boolean,
    public decisionChangeDeadlineDate: Date,
    public canPostponeReminder: boolean
  ) {}

  public get hasAlreadyMadeDecision(): boolean {
    return (
      this.confirmationState === PresenceStateEnum.Confirmed ||
      this.confirmationState === PresenceStateEnum.Denied
    );
  }

  public static fromPayload(payload?: any): VolunteerPresenceModel {
    return new VolunteerPresenceModel(
      this.resolvePresenceState(payload?.confirmationState),
      payload?.canDecisionBeChanged,
      new Date(payload?.decisionChangeDeadlineDate),
      payload?.canPostponeReminder
    );
  }

  private static resolvePresenceState(state: string): PresenceStateEnum {
    if (state === 'UNRESOLVED') {
      return PresenceStateEnum.Unresolved;
    }
    if (state === 'CONFIRMED') {
      return PresenceStateEnum.Confirmed;
    }
    if (state === 'DENIED') {
      return PresenceStateEnum.Denied;
    }
    throw new Error('Could not recognize presence state');
  }
}
