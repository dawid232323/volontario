import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageInstitutionWorkersComponent } from './manage-institution-workers.component';

describe('ManageInstitutionWorkersComponent', () => {
  let component: ManageInstitutionWorkersComponent;
  let fixture: ComponentFixture<ManageInstitutionWorkersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageInstitutionWorkersComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageInstitutionWorkersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
