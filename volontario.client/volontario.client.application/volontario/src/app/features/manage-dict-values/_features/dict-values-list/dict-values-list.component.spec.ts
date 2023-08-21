import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DictValuesListComponent } from './dict-values-list.component';

describe('DictValuesListComponent', () => {
  let component: DictValuesListComponent;
  let fixture: ComponentFixture<DictValuesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DictValuesListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DictValuesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
