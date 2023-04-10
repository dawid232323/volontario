import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-register-success-info',
  templateUrl: './register-success-info.component.html',
  styleUrls: ['./register-success-info.component.scss'],
})
export class RegisterSuccessInfoComponent implements OnInit {
  @Output() loginPageTransitionClicked = new EventEmitter<void>();
  constructor() {}

  ngOnInit(): void {}
}
