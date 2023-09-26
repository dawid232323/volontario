import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/service/user.service';
import { User, UserProfile } from 'src/app/core/model/user.model';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss'],
})
export class UserDetailsComponent implements OnInit {
  private _userProfile?: UserProfile;
  private _loggedUser?: User;
  private _userId: number;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute
  ) {
    this._userId = +this.activatedRoute.snapshot.params['user_id'];
  }

  ngOnInit(): void {
    this.downloadUserData();
  }

  public get userProfile(): UserProfile | undefined {
    return this._userProfile;
  }

  public get canEditProfile(): boolean {
    return this._loggedUser?.id === this._userProfile?.id;
  }

  private downloadUserData() {
    forkJoin([
      this.userService.getUserProfileDetails(this._userId),
      this.userService.getCurrentUserData(),
    ]).subscribe(([userProfileData, loggedUser]) => {
      this._userProfile = userProfileData;
      this._loggedUser = loggedUser;
    });
  }
}
