import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferApplyComponent } from 'src/app/features/offer-apply/offer-apply.component';

describe('OfferApplyComponent', () => {
  let component: OfferApplyComponent;
  let fixture: ComponentFixture<OfferApplyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OfferApplyComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(OfferApplyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
