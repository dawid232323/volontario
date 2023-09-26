import { Component, Input, OnInit } from '@angular/core';
import { UserProfile } from 'src/app/core/model/user.model';
import { isNil } from 'lodash';
import { Router } from '@angular/router';

@Component({
  selector: 'app-basic-user-details',
  templateUrl: './basic-user-details.component.html',
  styleUrls: ['./basic-user-details.component.scss'],
})
export class BasicUserDetailsComponent implements OnInit {
  @Input() userProfile?: UserProfile;
  @Input() canEditProfile = false;

  constructor(private router: Router) {}

  ngOnInit(): void {}

  public onInstitutionNameCLicked() {
    if (isNil(this.userProfile?.institutionId)) {
      return;
    }
    this.router.navigate(['institution', this.userProfile?.institutionId]);
  }

  protected readonly isNil = isNil;
}
