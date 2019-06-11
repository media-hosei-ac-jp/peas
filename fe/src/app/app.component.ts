import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './user.service';
import { User } from './user';
import { Location } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  user: User;
  hasRole = User.hasRole;

  constructor(private userService: UserService,
              private router: Router,
              private location: Location) {
    userService.me().subscribe(res=> {
      this.user = res as User;
      userService.setUser(res);
      if(location.path() == '' || location.path() == '/top') {
        if(User.hasRole(this.user,'Instructor')) {
          router.navigate(['admin-top']);
        }
        else {
          router.navigate(['review-top']);
        }
      }
    });

  }
}
