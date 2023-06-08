import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationDetailsRowComponent } from './application-details-row.component';

describe('ApplicationDetailsRowComponent', () => {
  let component: ApplicationDetailsRowComponent;
  let fixture: ComponentFixture<ApplicationDetailsRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApplicationDetailsRowComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApplicationDetailsRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
