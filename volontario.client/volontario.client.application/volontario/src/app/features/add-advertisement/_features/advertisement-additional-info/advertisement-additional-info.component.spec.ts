import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementAdditionalInfoComponent } from './advertisement-additional-info.component';

describe('AdvertisementAdditionalInfoComponent', () => {
  let component: AdvertisementAdditionalInfoComponent;
  let fixture: ComponentFixture<AdvertisementAdditionalInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdvertisementAdditionalInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdvertisementAdditionalInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
