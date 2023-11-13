import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionOfferPresenceVolunteersListComponent } from './institution-offer-presence-volunteers-list.component';

describe('InstitutionOfferPresenceVolunteersListComponent', () => {
  let component: InstitutionOfferPresenceVolunteersListComponent;
  let fixture: ComponentFixture<InstitutionOfferPresenceVolunteersListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InstitutionOfferPresenceVolunteersListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstitutionOfferPresenceVolunteersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
