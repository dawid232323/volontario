import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterBasicInfoFormComponent } from './register-basic-info-form.component';

describe('RegisterBasicInfoFormComponent', () => {
  let component: RegisterBasicInfoFormComponent;
  let fixture: ComponentFixture<RegisterBasicInfoFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterBasicInfoFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterBasicInfoFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
