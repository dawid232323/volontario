import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnonymizableDirective } from 'src/app/core/directive/anonymizable.directive';
import { PipeModule } from 'src/app/core/pipe/pipe.module';

@NgModule({
  declarations: [AnonymizableDirective],
  imports: [CommonModule, PipeModule],
  exports: [AnonymizableDirective],
})
export class DirectiveModule {}
