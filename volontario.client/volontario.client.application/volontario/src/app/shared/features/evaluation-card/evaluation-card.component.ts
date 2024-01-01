import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  InstitutionOfferEvaluation,
  OfferEvaluationIf,
  UserOfferEvaluation,
} from 'src/app/core/model/evaluation.model';
import { isNil } from 'lodash';
import { UserService } from 'src/app/core/service/user.service';
import { UserRoleEnum } from 'src/app/core/model/user-role.model';
import { map, Observable } from 'rxjs';

export interface EvaluationRemovalDataIf {
  volunteerId?: number;
  institutionId?: number;
  offerId: number;
  evaluation: OfferEvaluationIf;
}

@Component({
  selector: 'app-evaluation-card',
  templateUrl: './evaluation-card.component.html',
  styleUrls: ['./evaluation-card.component.scss'],
})
export class EvaluationCardComponent implements OnInit {
  @Input() evaluation!: OfferEvaluationIf;

  @Output() removeEvaluation = new EventEmitter<EvaluationRemovalDataIf>();

  @Output() editEvaluation = new EventEmitter<number>();

  constructor(private userService: UserService) {}

  ngOnInit(): void {}

  protected readonly Array = Array;
  protected readonly isNil = isNil;

  canManageEvaluation(): Observable<boolean> {
    let loggedUserId: number;
    let isLoggedUserAdminOrMod: boolean;

    return this.userService.getCurrentUserData().pipe(
      map(user => {
        loggedUserId = user.id;
        isLoggedUserAdminOrMod =
          user.hasUserRole(UserRoleEnum.Admin) ||
          user.hasUserRole(UserRoleEnum.Moderator);

        return (
          isLoggedUserAdminOrMod || loggedUserId === this.evaluation.evaluatorId
        );
      })
    );
  }

  onEditEvaluation(): void {
    this.editEvaluation.emit(this.evaluation.offerId);
  }

  onRemoveEvaluation(): void {
    if (this.evaluation instanceof UserOfferEvaluation) {
      this.removeEvaluation.emit({
        volunteerId: this.evaluation.userId,
        offerId: this.evaluation.offerId,
        evaluation: this.evaluation,
      });
    } else if (this.evaluation instanceof InstitutionOfferEvaluation) {
      this.removeEvaluation.emit({
        volunteerId: this.evaluation.evaluatorId,
        offerId: this.evaluation.offerId,
        evaluation: this.evaluation,
      });
    }
  }
}
