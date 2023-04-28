import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterContactPersonComponent } from './register-contact-person.component';

describe('RegisterContactPersonComponent', () => {
  let component: RegisterContactPersonComponent;
  let fixture: ComponentFixture<RegisterContactPersonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterContactPersonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterContactPersonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
