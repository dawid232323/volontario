import { Component, Input, OnInit } from '@angular/core';

export interface ConfirmVolunteerPresenceIf {
  volunteerId: number;
  fullName: string;
  hasTakenPart: boolean;
}

@Component({
  selector: 'app-institution-offer-presence-volunteers-list',
  templateUrl: './institution-offer-presence-volunteers-list.component.html',
  styleUrls: ['./institution-offer-presence-volunteers-list.component.scss'],
})
export class InstitutionOfferPresenceVolunteersListComponent implements OnInit {
  @Input() availableVolunteers: ConfirmVolunteerPresenceIf[] = [];

  public visibleColumns = ['fullName', 'hasTakenPart'];

  constructor() {}

  ngOnInit(): void {}
}
