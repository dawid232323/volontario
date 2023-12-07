import { Component, Input, OnInit } from '@angular/core';
import { LandingPageTile } from 'src/app/core/model/configuration.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-tile-form-row',
  templateUrl: './tile-form-table.component.html',
  styleUrls: ['./tile-form-table.component.scss'],
})
export class TileFormTableComponent implements OnInit {
  @Input() tiles: LandingPageTile[] = [];

  public formGroups: FormGroup[] = [];

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.tiles.forEach((tile, index) => {
      this.addFormGroup(tile);
      this.formGroups[index].patchValue({ ...tile });
    });
  }

  public onTileUp(index: number) {
    if (index > 0 && index < this.formGroups.length) {
      const temp = this.formGroups[index - 1];
      this.formGroups[index - 1] = this.formGroups[index];
      this.formGroups[index] = temp;
    }
  }

  public onTileDown(index: number) {
    if (index >= 0 && index < this.formGroups.length - 1) {
      const temp = this.formGroups[index + 1];
      this.formGroups[index + 1] = this.formGroups[index];
      this.formGroups[index] = temp;
    }
  }

  public onTileRemove(tileIndex: number) {
    this.tiles.splice(tileIndex, 1);
    this.formGroups.splice(tileIndex, 1);
  }

  public onAddTile() {
    this.addFormGroup();
    this.tiles.push(new LandingPageTile(null, null, null));
  }

  public isViewValid(): boolean {
    return this.formGroups.every(group => group.valid);
  }

  public get value(): LandingPageTile[] {
    return this.formGroups.map(group => group.value);
  }

  private addFormGroup(tile?: LandingPageTile) {
    this.formGroups.push(
      this.formBuilder.group({
        title: [
          tile?.title || null,
          [Validators.required, Validators.maxLength(100)],
        ],
        stepContent: [
          tile?.stepContent || null,
          [Validators.required, Validators.maxLength(500)],
        ],
        stepIcon: [tile?.stepIcon || null, [Validators.maxLength(30)]],
      })
    );
  }
}
