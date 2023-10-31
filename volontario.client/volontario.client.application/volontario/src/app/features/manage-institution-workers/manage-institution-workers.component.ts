import { Component, OnInit, ViewChild } from '@angular/core';
import { InstitutionService } from 'src/app/core/service/institution.service';
import { UserService } from 'src/app/core/service/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { InstitutionWorker, User } from 'src/app/core/model/user.model';
import {
  Institution,
  RegisterInstitutionEmployeeDto,
} from 'src/app/core/model/institution.model';
import { firstValueFrom, forkJoin } from 'rxjs';
import {
  InstitutionWorkersListComponent,
  WorkerRoleChangeEventIf,
} from 'src/app/features/manage-institution-workers/_features/institution-workers-list/institution-workers-list.component';
import { MatDialog } from '@angular/material/dialog';
import {
  ConfirmationAlertComponent,
  ConfirmationAlertInitialData,
  ConfirmationAlertResult,
  ConfirmationAlertResultIf,
} from 'src/app/shared/features/confirmation-alert/confirmation-alert.component';
import { AddEmployeeFormComponent } from 'src/app/features/manage-institution-workers/_features/add-employee-form/add-employee-form.component';
import { isNil } from 'lodash';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-manage-institution-workers',
  templateUrl: './manage-institution-workers.component.html',
  styleUrls: ['./manage-institution-workers.component.scss'],
})
export class ManageInstitutionWorkersComponent implements OnInit {
  private _institutionId: number;
  private _institutionWorkers: InstitutionWorker[] = [];
  private _institution?: Institution;
  private _user?: User;

  @ViewChild('workersList')
  private _workersListComponent?: InstitutionWorkersListComponent;

  constructor(
    private institutionService: InstitutionService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private dialog: MatDialog
  ) {
    this._institutionId =
      +this.activatedRoute.snapshot.params['institution_id'];
  }

  ngOnInit(): void {
    forkJoin([
      this.institutionService.getInstitutionWorkers(this._institutionId),
      this.institutionService.getInstitutionDetails(this._institutionId),
      this.userService.getCurrentUserData(),
    ]).subscribe(([workers, institution, user]) => {
      this._institutionWorkers = workers;
      this._institution = institution;
      this._user = user;
    });
  }

  public get institutionWorkers(): InstitutionWorker[] {
    return this._institutionWorkers;
  }

  public get institution(): Institution | undefined {
    return this._institution;
  }

  public get user(): User | undefined {
    return this._user;
  }

  public onReturnToDetailsButtonClicked() {
    this.router.navigate(['institution', this._institutionId]);
  }

  public async onRoleChangeEvent(event: WorkerRoleChangeEventIf) {
    const initialData: ConfirmationAlertInitialData = {
      confirmationMessage: `Czy na pewno chcesz zmienić rolę ${event.worker.firstName} ${event.worker.lastName}?`,
    };
    const dialogRef = this.dialog.open(ConfirmationAlertComponent, {
      data: initialData,
    });
    const confirmationResult: ConfirmationAlertResultIf = await firstValueFrom(
      dialogRef.afterClosed()
    );
    if (
      confirmationResult.confirmationAlertResult !==
      ConfirmationAlertResult.Accept
    ) {
      return;
    }
    this.institutionService
      .changeInstitutionWorkerRole(
        event.worker.id,
        this._institutionId,
        event.operation
      )
      .subscribe(async () => {
        this._institutionWorkers = await firstValueFrom(
          this.institutionService.getInstitutionWorkers(this._institutionId)
        );
      });
  }

  public async onAddWorkerButtonClicked() {
    const dialogRef = this.dialog.open(AddEmployeeFormComponent, {
      data: { institutionId: this._institutionId },
    });
    const formResult: RegisterInstitutionEmployeeDto = await firstValueFrom(
      dialogRef.afterClosed()
    );
    if (isNil(formResult.institutionId)) {
      return;
    }
    this.institutionService.registerInstitutionEmployee(formResult).subscribe({
      next: (result: User) => {
        const createdWorker = InstitutionWorker.fromUser(result);
        this._workersListComponent?.addWorker(createdWorker);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 207) {
          alert(
            'Konto użytkownika/czki zostało stworzone poprawnie, ale nie została wysłana do niego wiadomość email. Skontaktuj się z administracją'
          );
        } else {
          alert(JSON.stringify(error.error));
        }
      },
    });
  }
}
