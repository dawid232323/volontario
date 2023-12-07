import { Component, HostListener, OnInit } from '@angular/core';
import {
  ConfigurationImages,
  ImageCategories,
  LandingPageDto,
  LandingPageTile,
} from 'src/app/core/model/configuration.model';
import { ConfigurationService } from 'src/app/core/service/configuration.service';
import { ActivatedRoute } from '@angular/router';
import { isNil } from 'lodash';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss'],
})
export class MainPageComponent implements OnInit {
  public mainPageContent?: LandingPageDto;
  public previewMode = false;
  public windowWidth = window.innerWidth;
  constructor(
    private configurationService: ConfigurationService,
    private activatedRoute: ActivatedRoute
  ) {}

  @HostListener('window:resize')
  onResize() {
    this.windowWidth = window.innerWidth;
    console.log(this.windowWidth);
  }

  ngOnInit(): void {
    this.initMainPageContent();
  }

  public getGridColsClass(tiles: LandingPageTile[]): string {
    if (tiles.length > 4) {
      return 'lg:grid-cols-4';
    }
    return `lg:grid-cols-${tiles.length}`;
  }

  public getPaddingTopClass(tiles: LandingPageTile[]): string {
    if (this.windowWidth < 640) {
      return '4rem';
    }
    return `${4 * (Math.floor(tiles.length / 5) + 1)}rem`;
  }

  public getPaddingBottomStyle(tiles: LandingPageTile[]): string {
    if (this.windowWidth < 640) {
      return '0rem';
    }
    return `${4 * (Math.floor(tiles.length / 5) + 1)}rem`;
  }

  public getPaddingBottomClass(tiles: LandingPageTile[]): string {
    return `pb-${10 * (Math.floor(tiles.length / 5) + 1) + 4}`;
  }

  public getWidthClass(tiles: LandingPageTile[]) {
    if (tiles.length === 0) {
      return '';
    }
    return 'w-full';
  }

  public resolveImagePath(imageCategory: ImageCategories | null): string {
    if (isNil(imageCategory)) {
      return ConfigurationImages[0].path;
    }
    return (
      ConfigurationImages.find(confImage => confImage.type === imageCategory)
        ?.path || ConfigurationImages[0].path
    );
  }

  private initMainPageContent() {
    this.previewMode = <boolean>(
      this.activatedRoute.snapshot.queryParams['prev']
    );
    if (this.previewMode) {
      this.mainPageContent = this.configurationService.landingPagePreview;
    } else {
      this.configurationService.getLandingPageData().subscribe(data => {
        this.mainPageContent = data;
      });
    }
  }

  protected readonly isNil = isNil;
}
