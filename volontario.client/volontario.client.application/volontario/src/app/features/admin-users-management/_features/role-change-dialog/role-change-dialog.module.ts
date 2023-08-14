import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoleChangeDialogComponent } from './role-change-dialog.component';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [RoleChangeDialogComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
  ],
  exports: [RoleChangeDialogComponent],
})
export class RoleChangeDialogModule {}
