import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementBasicInfoComponent } from './advertisement-basic-info.component';

describe('AdvertisementBasicInfoComponent', () => {
  let component: AdvertisementBasicInfoComponent;
  let fixture: ComponentFixture<AdvertisementBasicInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdvertisementBasicInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdvertisementBasicInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
