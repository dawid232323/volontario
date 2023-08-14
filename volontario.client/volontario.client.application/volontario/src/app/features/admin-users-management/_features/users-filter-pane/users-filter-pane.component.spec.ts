import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersFilterPaneComponent } from 'src/app/features/admin-users-management/_features/users-filter-pane/users-filter-pane.component';

describe('UsersFilterPaneComponent', () => {
  let component: UsersFilterPaneComponent;
  let fixture: ComponentFixture<UsersFilterPaneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsersFilterPaneComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(UsersFilterPaneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
