import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadProfilePictureModalComponent } from './upload-profile-picture-modal.component';

describe('UploadProfilePictureModalComponent', () => {
  let component: UploadProfilePictureModalComponent;
  let fixture: ComponentFixture<UploadProfilePictureModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UploadProfilePictureModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(UploadProfilePictureModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
