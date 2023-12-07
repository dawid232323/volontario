import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SectionFormComponent } from 'src/app/features/main-page-editor/_features/section-form/section-form.component';

describe('SectionFormComponent', () => {
  let component: SectionFormComponent;
  let fixture: ComponentFixture<SectionFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SectionFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SectionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
