import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersFilterPaneComponent } from 'src/app/features/admin-users-management/_features/users-filter-pane/users-filter-pane.component';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [UsersFilterPaneComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
  ],
  exports: [UsersFilterPaneComponent],
})
export class UsersFilterPaneModule {}
