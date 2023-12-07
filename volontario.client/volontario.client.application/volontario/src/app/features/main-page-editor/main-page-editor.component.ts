import {
  ChangeDetectorRef,
  Component,
  OnInit,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  ImageCategories,
  LandingPageDto,
  LandingPageSection,
  LandingPageTile,
  SectionType,
} from 'src/app/core/model/configuration.model';
import { ConfigurationService } from 'src/app/core/service/configuration.service';
import { Subscription } from 'rxjs';
import { SectionFormComponent } from 'src/app/features/main-page-editor/_features/section-form/section-form.component';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-main-page-editor',
  templateUrl: './main-page-editor.component.html',
  styleUrls: ['./main-page-editor.component.scss'],
})
export class MainPageEditorComponent implements OnInit {
  @ViewChildren(SectionFormComponent)
  sectionForms!: QueryList<SectionFormComponent>;

  private _pageContent: LandingPageDto = { sections: [] };

  constructor(
    private configurationService: ConfigurationService,
    private changeDetector: ChangeDetectorRef,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.downloadData();
  }

  public canNotSubmit(): boolean {
    return this.sectionForms?.some(f => !f.isFormValid()) || false;
  }

  private downloadData() {
    if (<boolean>this.activatedRoute.snapshot.queryParams['prev']) {
      this._pageContent = <LandingPageDto>(
        this.configurationService.landingPagePreview
      );
    } else {
      this.configurationService.getLandingPageData().subscribe(result => {
        this._pageContent = result;
      });
    }
  }

  public onShowPreview() {
    const sections = this.sectionForms.map(form => form.value);
    this.configurationService.landingPagePreview = new LandingPageDto(sections);
    this.router.navigate(['/'], { queryParams: { prev: true } });
  }

  public onSubmitChanges() {
    if (this.canNotSubmit()) {
      return;
    }
    const sections = this.sectionForms.map(form => form.value);
    const finalDto = new LandingPageDto(sections);
    this.configurationService.saveNewLandingPage(finalDto).subscribe(() => {
      this.router.navigate(['/']);
    });
  }

  public moveSectionUp(sectionToMove: LandingPageSection) {
    const index = this._pageContent.sections.findIndex(
      section => section.id === sectionToMove.id
    );
    if (index > 0 && index < this._pageContent.sections.length) {
      const temp = this._pageContent.sections[index - 1];
      this._pageContent.sections[index - 1] = this._pageContent.sections[index];
      this._pageContent.sections[index] = temp;
    }
  }

  public moveSectionDown(sectionToMove: LandingPageSection) {
    const index = this._pageContent.sections.findIndex(
      section => section.id === sectionToMove.id
    );
    if (index >= 0 && index < this._pageContent.sections.length - 1) {
      const temp = this._pageContent.sections[index + 1];
      this._pageContent.sections[index + 1] = this._pageContent.sections[index];
      this._pageContent.sections[index] = temp;
    }
  }

  public removeSection(sectionIndex: number) {
    this._pageContent.sections.splice(sectionIndex, 1);
  }

  public addSection() {
    this._pageContent.sections.push(
      new LandingPageSection(
        null,
        null,
        <SectionType>'TextOnly',
        null,
        ImageCategories.Default,
        []
      )
    );
    this.changeDetector.detectChanges();
  }

  public get pageContent(): LandingPageDto {
    return this._pageContent;
  }
}
