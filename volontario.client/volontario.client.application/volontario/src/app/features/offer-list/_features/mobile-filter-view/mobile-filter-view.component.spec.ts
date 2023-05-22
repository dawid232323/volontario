import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MobileFilterViewComponent } from './mobile-filter-view.component';

describe('MobileFilterViewComponent', () => {
  let component: MobileFilterViewComponent;
  let fixture: ComponentFixture<MobileFilterViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MobileFilterViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MobileFilterViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
