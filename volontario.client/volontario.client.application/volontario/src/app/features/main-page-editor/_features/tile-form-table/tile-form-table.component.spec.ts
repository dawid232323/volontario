import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TileFormTableComponent } from 'src/app/features/main-page-editor/_features/tile-form-table/tile-form-table.component';

describe('TileFormRowComponent', () => {
  let component: TileFormTableComponent;
  let fixture: ComponentFixture<TileFormTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TileFormTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TileFormTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
