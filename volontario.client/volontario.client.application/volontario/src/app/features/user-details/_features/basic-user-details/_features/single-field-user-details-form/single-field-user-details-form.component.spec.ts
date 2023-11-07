import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleFieldUserDetailsFormComponent } from './single-field-user-details-form.component';

describe('SingleFieldUserDetailsFormComponent', () => {
  let component: SingleFieldUserDetailsFormComponent;
  let fixture: ComponentFixture<SingleFieldUserDetailsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SingleFieldUserDetailsFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SingleFieldUserDetailsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
