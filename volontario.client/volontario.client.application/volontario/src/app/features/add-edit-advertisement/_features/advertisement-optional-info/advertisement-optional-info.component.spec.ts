import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementOptionalInfoComponent } from './advertisement-optional-info.component';

describe('AdvertisementOptionalInfoComponent', () => {
  let component: AdvertisementOptionalInfoComponent;
  let fixture: ComponentFixture<AdvertisementOptionalInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdvertisementOptionalInfoComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AdvertisementOptionalInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
