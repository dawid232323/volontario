import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApplicationDetailsRowComponent } from 'src/app/features/advertisement-details/_features/application-list/_features/application-details-row/application-details-row.component';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { MatChipsModule } from '@angular/material/chips';
import { DirectiveModule } from 'src/app/core/directive/directive.module';

@NgModule({
  declarations: [ApplicationDetailsRowComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatListModule,
    MatChipsModule,
    DirectiveModule,
  ],
  exports: [ApplicationDetailsRowComponent],
})
export class ApplicationDetailsRowModule {}
