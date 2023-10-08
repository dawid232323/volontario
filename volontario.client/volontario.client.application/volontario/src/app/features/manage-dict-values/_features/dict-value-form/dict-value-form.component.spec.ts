import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DictValueFormComponent } from './dict-value-form.component';

describe('DictValueFormComponent', () => {
  let component: DictValueFormComponent;
  let fixture: ComponentFixture<DictValueFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DictValueFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DictValueFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
