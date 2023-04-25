import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainPageComponent } from './main-page.component';
import { MainPageNavModule } from './nav/nav.module';
import { FooterModule } from '../../shared/features/footer/footer.module';

@NgModule({
  declarations: [MainPageComponent],
  imports: [CommonModule, MainPageNavModule, FooterModule],
  exports: [MainPageComponent],
})
export class MainPageModule {}
