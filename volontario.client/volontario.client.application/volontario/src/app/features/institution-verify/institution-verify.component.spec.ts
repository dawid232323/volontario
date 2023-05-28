import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionVerifyComponent } from './institution-verify.component';

describe('InstitutionVerifyComponent', () => {
  let component: InstitutionVerifyComponent;
  let fixture: ComponentFixture<InstitutionVerifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InstitutionVerifyComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(InstitutionVerifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
