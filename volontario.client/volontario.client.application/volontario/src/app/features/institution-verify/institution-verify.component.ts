import { Component, OnInit } from '@angular/core';
import {
  InstitutionVerifyConst,
  VerifyType,
} from 'src/app/features/institution-verify/institution-verify.const';
import { ActivatedRoute, Router } from '@angular/router';
import { InstitutionService } from 'src/app/core/service/institution.service';

@Component({
  selector: 'app-institution-verify',
  templateUrl: './institution-verify.component.html',
  styleUrls: ['./institution-verify.component.scss'],
})
export class InstitutionVerifyComponent implements OnInit {
  public hasEndedOperation: boolean = false;
  private institutionToken?: string;
  public operationType?: VerifyType;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private institutionService: InstitutionService
  ) {}

  ngOnInit(): void {
    this.determineOperationType();
    this.institutionToken = this.activatedRoute.snapshot.queryParams['t'];
    this.institutionService
      .verifyInstitution(this.institutionToken!, this.operationType)
      .subscribe({
        next: () => (this.hasEndedOperation = true),
        error: err => alert(err.message),
      });
  }

  public get title() {
    if (this.operationType === VerifyType.REJECT) {
      return InstitutionVerifyConst.rejectTitle;
    }
    return InstitutionVerifyConst.acceptTitle;
  }

  public get content() {
    if (this.operationType === VerifyType.REJECT) {
      return InstitutionVerifyConst.rejectContent;
    }
    return InstitutionVerifyConst.acceptContent;
  }

  private determineOperationType(): void {
    const operationString: string =
      this.activatedRoute.snapshot.queryParams['a'];
    if (operationString === VerifyType.ACCEPT) {
      this.operationType = VerifyType.ACCEPT;
      return;
    }
    if (operationString === VerifyType.REJECT) {
      this.operationType = VerifyType.REJECT;
      return;
    }
    this.router.navigate(['/home']);
  }

  public onCardButtonClicked(): Promise<boolean> {
    return this.router.navigate(['home']);
  }
}
