import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetPasswordComponent } from 'src/app/shared/features/set-password-form/set-password.component';

describe('RegisterContactPersonFormComponent', () => {
  let component: SetPasswordComponent;
  let fixture: ComponentFixture<SetPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SetPasswordComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
