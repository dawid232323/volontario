import { Directive, EventEmitter, HostListener, Output } from '@angular/core';

@Directive({
  selector: '[appDragAndDrop]',
})
export class DragAndDropDirective {
  @Output() whenFileDropped = new EventEmitter<any>();

  @HostListener('dragover', ['$event']) onDragOver(event: DragEvent): void {
    this.onAnyDragEvent(event);
  }

  @HostListener('dragleave', ['$event'])
  onDragLeave(event: DragEvent): void {
    this.onAnyDragEvent(event);
  }

  @HostListener('drop', ['$event'])
  ondrop(event: DragEvent): void {
    this.onAnyDragEvent(event);
    let files = event.dataTransfer!.files;
    if (files.length > 0) {
      this.whenFileDropped.emit(files);
    }
  }

  onAnyDragEvent(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
  }
}
