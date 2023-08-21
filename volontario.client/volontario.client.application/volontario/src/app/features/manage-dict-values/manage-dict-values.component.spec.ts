import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageDictValuesComponent } from './manage-dict-values.component';

describe('ManageDictValuesComponent', () => {
  let component: ManageDictValuesComponent;
  let fixture: ComponentFixture<ManageDictValuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageDictValuesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageDictValuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
