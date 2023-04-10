import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterSuccessInfoComponent } from './register-success-info.component';

describe('RegisterSuccessInfoComponent', () => {
  let component: RegisterSuccessInfoComponent;
  let fixture: ComponentFixture<RegisterSuccessInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterSuccessInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterSuccessInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
