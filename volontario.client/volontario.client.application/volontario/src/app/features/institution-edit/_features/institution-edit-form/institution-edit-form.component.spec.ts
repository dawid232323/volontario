import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionEditFormComponent } from './institution-edit-form.component';

describe('InstitutionEditFormComponent', () => {
  let component: InstitutionEditFormComponent;
  let fixture: ComponentFixture<InstitutionEditFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InstitutionEditFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(InstitutionEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
