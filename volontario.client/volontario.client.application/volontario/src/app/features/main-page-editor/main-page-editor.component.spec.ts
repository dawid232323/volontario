import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainPageEditorComponent } from './main-page-editor.component';

describe('MainPageEditorComponent', () => {
  let component: MainPageEditorComponent;
  let fixture: ComponentFixture<MainPageEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MainPageEditorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MainPageEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
