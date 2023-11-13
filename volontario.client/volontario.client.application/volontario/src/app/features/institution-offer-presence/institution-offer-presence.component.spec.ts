import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionOfferPresenceComponent } from './institution-offer-presence.component';

describe('InstitutionOfferPresenceComponent', () => {
  let component: InstitutionOfferPresenceComponent;
  let fixture: ComponentFixture<InstitutionOfferPresenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InstitutionOfferPresenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstitutionOfferPresenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
