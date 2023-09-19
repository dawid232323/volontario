import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionWorkersListComponent } from './institution-workers-list.component';

describe('InstitutionWorkersListComponent', () => {
  let component: InstitutionWorkersListComponent;
  let fixture: ComponentFixture<InstitutionWorkersListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InstitutionWorkersListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstitutionWorkersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
