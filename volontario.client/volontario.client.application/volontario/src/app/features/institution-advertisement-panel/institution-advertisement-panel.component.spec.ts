import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionAdvertisementPanelComponent } from './institution-advertisement-panel.component';

describe('InstitutionAdvertisementPanelComponent', () => {
  let component: InstitutionAdvertisementPanelComponent;
  let fixture: ComponentFixture<InstitutionAdvertisementPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InstitutionAdvertisementPanelComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstitutionAdvertisementPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
