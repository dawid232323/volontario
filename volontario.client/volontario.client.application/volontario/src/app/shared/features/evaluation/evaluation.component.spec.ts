import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluationComponent } from 'src/app/shared/features/evaluation/evaluation.component';

describe('UserEvaluationComponent', () => {
  let component: EvaluationComponent;
  let fixture: ComponentFixture<EvaluationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EvaluationComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EvaluationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
