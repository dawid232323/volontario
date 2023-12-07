import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TileFormTableComponent } from 'src/app/features/main-page-editor/_features/tile-form-table/tile-form-table.component';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [TileFormTableComponent],
  imports: [
    CommonModule,
    MatTableModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
  ],
  exports: [TileFormTableComponent],
})
export class TileFormTableModule {}
