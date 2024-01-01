import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VolunteerPresenceListComponent } from './volunteer-presence-list.component';

describe('VolunteerPresenceListComponent', () => {
  let component: VolunteerPresenceListComponent;
  let fixture: ComponentFixture<VolunteerPresenceListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VolunteerPresenceListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VolunteerPresenceListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
