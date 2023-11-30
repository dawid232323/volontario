import { Component, Input, OnInit } from '@angular/core';
import { OfferEvaluationIf } from 'src/app/core/model/evaluation.model';
import { isNil } from 'lodash';

@Component({
  selector: 'app-evaluation-card',
  templateUrl: './evaluation-card.component.html',
  styleUrls: ['./evaluation-card.component.scss'],
})
export class EvaluationCardComponent implements OnInit {
  @Input() evaluation!: OfferEvaluationIf;

  constructor() {}

  ngOnInit(): void {}

  protected readonly Array = Array;
  protected readonly isNil = isNil;
}
