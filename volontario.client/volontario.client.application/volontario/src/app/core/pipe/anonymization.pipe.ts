import { Injectable, Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'anonymize',
})
@Injectable()
export class AnonymizationPipe implements PipeTransform {
  transform(value: any, shouldAnonymize: boolean): any {
    if (shouldAnonymize) {
      return '********';
    }
    return value;
  }
}
