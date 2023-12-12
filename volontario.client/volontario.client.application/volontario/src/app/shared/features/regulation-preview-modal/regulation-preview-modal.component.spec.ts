import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegulationPreviewModalComponent } from './regulation-preview-modal.component';

describe('RegulationPreviewModalComponent', () => {
  let component: RegulationPreviewModalComponent;
  let fixture: ComponentFixture<RegulationPreviewModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegulationPreviewModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegulationPreviewModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
