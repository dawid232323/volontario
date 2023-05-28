import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementPreviewCardComponent } from './advertisement-preview-card.component';

describe('AdvertisementPreviewCardComponent', () => {
  let component: AdvertisementPreviewCardComponent;
  let fixture: ComponentFixture<AdvertisementPreviewCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdvertisementPreviewCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AdvertisementPreviewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
