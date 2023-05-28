import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordCheckCardComponent } from './password-check-card.component';

describe('PasswordCheckCardComponent', () => {
  let component: PasswordCheckCardComponent;
  let fixture: ComponentFixture<PasswordCheckCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PasswordCheckCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PasswordCheckCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
