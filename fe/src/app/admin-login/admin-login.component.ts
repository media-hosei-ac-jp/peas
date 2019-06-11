import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-admin-login',
  templateUrl: './admin-login.component.html',
  styleUrls: ['./admin-login.component.css']
})
export class AdminLoginComponent implements OnInit {
  private uid;
  successful;
  users$;

  constructor(private adminService: AdminService,
              private userService: UserService) { }

  ngOnInit() {
    this.users$ = this.userService.getAllByLtiResourceLink();
  }

  login() {
    if(!this.uid) {
      return;
    }
    this._login(this.uid);
  }

  _login(uid) {
    this.adminService.login4I(uid).subscribe(res=>{
        this.successful = true;
      });
  }

}
