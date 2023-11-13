import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VolunteerConfirmPresenceComponent } from './volunteer-confirm-presence.component';

describe('VolunteerConfirmPresenceComponent', () => {
  let component: VolunteerConfirmPresenceComponent;
  let fixture: ComponentFixture<VolunteerConfirmPresenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VolunteerConfirmPresenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VolunteerConfirmPresenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
