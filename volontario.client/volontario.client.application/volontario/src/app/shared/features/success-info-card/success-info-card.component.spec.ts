import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessInfoCardComponent } from './success-info-card.component';

describe('SuccessInfoCardComponent', () => {
  let component: SuccessInfoCardComponent;
  let fixture: ComponentFixture<SuccessInfoCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SuccessInfoCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SuccessInfoCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
