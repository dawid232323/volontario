import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopFilterPanelComponent } from './top-filter-panel.component';

describe('TopFilterPanelComponent', () => {
  let component: TopFilterPanelComponent;
  let fixture: ComponentFixture<TopFilterPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopFilterPanelComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopFilterPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
