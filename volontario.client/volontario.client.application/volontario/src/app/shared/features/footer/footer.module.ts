import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './footer.component';
import { AppRoutingModule } from '../../../app-routing.module';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [FooterComponent],
  imports: [CommonModule, AppRoutingModule, MatButtonModule],
  exports: [FooterComponent],
})
export class FooterModule {}
