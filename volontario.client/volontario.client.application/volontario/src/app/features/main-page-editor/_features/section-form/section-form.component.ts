import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import {
  ConfigurationImages,
  ImageCategories,
  LandingPageSection,
  LandingPageTile,
  SectionType,
} from 'src/app/core/model/configuration.model';
import { DefaultAngularEditorConfigProvider } from 'src/app/utils/angular-editor.const';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { TileFormTableComponent } from 'src/app/features/main-page-editor/_features/tile-form-table/tile-form-table.component';

@Component({
  selector: 'app-section-form',
  templateUrl: './section-form.component.html',
  styleUrls: ['./section-form.component.scss'],
})
export class SectionFormComponent implements OnInit, OnDestroy {
  @ViewChild('tileForm') tileForm?: TileFormTableComponent;

  @Input() section!: LandingPageSection;
  @Input() canMoveUp: boolean = true;
  @Input() canMoveDown: boolean = true;
  @Output() sectionChange: EventEmitter<LandingPageSection> =
    new EventEmitter<LandingPageSection>();

  @Output() sectionDownEvent = new EventEmitter<void>();
  @Output() sectionUpEvent = new EventEmitter<void>();
  @Output() sectionRemovedEvent = new EventEmitter<void>();

  editorConfig = new DefaultAngularEditorConfigProvider().config;
  idFormControl!: FormControl;
  titleFormControl!: FormControl;
  typeFormControl!: FormControl;
  contentFormControl!: FormControl;
  imageFormControl!: FormControl;

  private subscriptions = new Subscription();
  private formControls: FormControl[] = [];
  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.initFormControls();
    this.makeSubscriptions();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  areTilesSelected(sectionTypeValue: string): boolean {
    const enumKey = Object.keys(SectionType).find(
      key => key === sectionTypeValue
    );
    // @ts-ignore
    return SectionType[enumKey] === SectionType.WithTiles;
  }

  isFormValid(): boolean {
    const areBasicControlsValid =
      this.formControls.every(control => control.valid) || false;
    if (!this.areTilesSelected(this.section.type)) {
      return areBasicControlsValid;
    }
    const areTileValid = this.tileForm?.isViewValid();
    return (areTileValid && areBasicControlsValid) || false;
  }

  private initFormControls() {
    this.idFormControl = this.formBuilder.control(this.section?.id || null, [
      Validators.required,
      Validators.maxLength(50),
    ]);
    this.titleFormControl = this.formBuilder.control(
      this.section?.title || null,
      [Validators.required, Validators.maxLength(100)]
    );
    this.typeFormControl = this.formBuilder.control(
      this.section?.type || null,
      [Validators.required]
    );
    this.contentFormControl = this.formBuilder.control(
      this.section?.content || null,
      [Validators.required, Validators.maxLength(2000)]
    );
    this.imageFormControl = this.formBuilder.control(
      this.section?.imageCategory || ImageCategories.Default
    );
    this.formControls.push(
      this.idFormControl,
      this.titleFormControl,
      this.typeFormControl,
      this.contentFormControl,
      this.imageFormControl
    );
  }

  private makeSubscriptions() {
    this.subscriptions.add(
      this.typeFormControl.valueChanges.subscribe(this.onTypeChange.bind(this))
    );
  }

  private onTypeChange() {
    if (this.typeFormControl.value === SectionType.TextOnly) {
      this.contentFormControl?.addValidators(Validators.required);
    } else {
      this.contentFormControl?.removeValidators(Validators.required);
    }
    this.contentFormControl?.updateValueAndValidity();
  }

  public get value(): LandingPageSection {
    const tileValues: LandingPageTile[] = [];
    let sectionContent = null;
    if (this.areTilesSelected(this.section.type)) {
      tileValues.push(...(this.tileForm?.value || []));
    } else {
      sectionContent = this.contentFormControl?.value;
    }
    return new LandingPageSection(
      this.idFormControl.value,
      this.titleFormControl.value,
      this.typeFormControl.value,
      sectionContent,
      this.imageFormControl.value,
      tileValues
    );
  }

  protected readonly Object = Object;
  protected readonly SectionType = SectionType;
  protected readonly ConfigurationImages = ConfigurationImages;
}
