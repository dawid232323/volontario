import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterContactPersonFormComponent } from './register-contact-person-form.component';

describe('RegisterContactPersonFormComponent', () => {
  let component: RegisterContactPersonFormComponent;
  let fixture: ComponentFixture<RegisterContactPersonFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterContactPersonFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterContactPersonFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
