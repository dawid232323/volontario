import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterAdditionalInfoFormComponent } from './register-additional-info-form.component';

describe('RegisterAdditionalInfoFormComponent', () => {
  let component: RegisterAdditionalInfoFormComponent;
  let fixture: ComponentFixture<RegisterAdditionalInfoFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterAdditionalInfoFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterAdditionalInfoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
