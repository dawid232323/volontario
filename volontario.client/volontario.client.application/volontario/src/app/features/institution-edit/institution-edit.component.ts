import { Component, OnInit } from '@angular/core';
import { Institution } from 'src/app/core/model/institution.model';
import { InstitutionService } from 'src/app/core/service/institution.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-institution-edit',
  templateUrl: './institution-edit.component.html',
  styleUrls: ['./institution-edit.component.scss'],
})
export class InstitutionEditComponent implements OnInit {
  private readonly _institutionId;
  private _isLoadingData = true;
  private _institutionDetails?: Institution;

  constructor(private institutionService: InstitutionService, private activatedRoute: ActivatedRoute, private router: Router) {
    this._institutionId = this.activatedRoute.snapshot.params['institution_id'];
  }

  public ngOnInit(): void {
    this.setFormData();
  }

  public get institutionDetails(): Institution | undefined {
    return this._institutionDetails;
  }

  public get isLoadingData(): boolean {
    return this._isLoadingData;
  }

  public onEditFormSubmit(institutionData: Institution) {
    this.institutionService.editInstitutionData(this._institutionId, institutionData).subscribe({
      next: () => {
        this.router.navigate(['institution', this._institutionId]);
      },
      error: (error: HttpErrorResponse) => alert(error.error),
    });
  }

  private setFormData() {
    this.institutionService.getInstitutionDetails(this._institutionId).subscribe(institutionResult => {
      this._institutionDetails = institutionResult;
      this._isLoadingData = false;
    });
  }
}
