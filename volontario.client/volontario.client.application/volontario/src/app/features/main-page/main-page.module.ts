import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainPageComponent } from './main-page.component';
import { MainPageNavModule } from './nav/nav.module';
import { FooterModule } from '../../shared/features/footer/footer.module';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [MainPageComponent],
  imports: [
    CommonModule,
    MainPageNavModule,
    FooterModule,
    MatIconModule,
    MatInputModule,
  ],
  exports: [MainPageComponent],
})
export class MainPageModule {}
