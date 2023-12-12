import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegulationsEditorComponent } from './regulations-editor.component';

describe('RegulationsEditorComponent', () => {
  let component: RegulationsEditorComponent;
  let fixture: ComponentFixture<RegulationsEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegulationsEditorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegulationsEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
