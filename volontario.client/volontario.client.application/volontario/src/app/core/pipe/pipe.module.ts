import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnonymizationPipe } from 'src/app/core/pipe/anonymization.pipe';

@NgModule({
  declarations: [AnonymizationPipe],
  imports: [CommonModule],
  exports: [AnonymizationPipe],
})
export class PipeModule {}
