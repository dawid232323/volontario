import {
  Directive,
  ElementRef,
  EventEmitter,
  HostBinding,
  HostListener,
  Input,
  Output,
} from '@angular/core';
import { AnonymizationPipe } from 'src/app/core/pipe/anonymization.pipe';

@Directive({
  selector: '[appAnonymizable]',
  providers: [AnonymizationPipe],
})
export class AnonymizableDirective {
  @Input() shouldAnonymize = true;
  @Input() valueToAnonymize: any = null;
  @Output() shouldAnonymizeChange = new EventEmitter<boolean>();

  @HostBinding('style.cursor')
  cursorType: string = 'pointer';

  @HostBinding('style.display')
  displayStyle: string = 'flex';

  @HostBinding('style.flex-direction')
  styleFlexDirection: string = 'row';

  @HostBinding('style.gap')
  styleFlexGap: string = '0.5rem';

  @HostListener('click')
  public onClickEvent() {
    this.shouldAnonymize = !this.shouldAnonymize;
    this.setAnonymizeIcon();
  }

  @HostListener('mouseenter')
  public onMouseEnter() {
    this.elementRef.nativeElement.classList?.add(this._textHoverClass);
  }

  @HostListener('mouseleave')
  public onMouseLeave() {
    this.elementRef.nativeElement.classList?.remove(this._textHoverClass);
  }

  private _textHoverClass = 'text-btnAccentPrimary';

  constructor(
    private elementRef: ElementRef,
    private anonymizePipe: AnonymizationPipe
  ) {
    this.setAnonymizeIcon();
  }

  private setAnonymizeIcon() {
    if (!this.shouldAnonymize) {
      this.elementRef.nativeElement.innerHTML = this.anonymizePipe
        .transform(this.valueToAnonymize, this.shouldAnonymize)
        .concat('<i class="fa-regular fa-eye-slash"></i>');
    } else {
      this.elementRef.nativeElement.innerHTML = this.anonymizePipe
        .transform(this.valueToAnonymize, this.shouldAnonymize)
        .concat('<i class="fa-regular fa-eye"></i>');
    }
  }
}
