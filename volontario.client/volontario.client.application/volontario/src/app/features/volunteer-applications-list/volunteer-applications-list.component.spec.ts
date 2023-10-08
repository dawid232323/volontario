import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VolunteerApplicationsListComponent } from './volunteer-applications-list.component';

describe('VolunteerApplicationsListComponent', () => {
  let component: VolunteerApplicationsListComponent;
  let fixture: ComponentFixture<VolunteerApplicationsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VolunteerApplicationsListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VolunteerApplicationsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
