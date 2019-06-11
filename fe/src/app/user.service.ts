import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { environment } from '../environments/environment';
import { User } from './user';

@Injectable()
export class UserService {
  private apiUrl = environment.beUrl + 'api/user';
  private options = new RequestOptions({ withCredentials: true });

  private user: User;

  constructor(private http: Http) {
  }

  me() {
    const url: string = `${this.apiUrl}/me`;
    return this.http.get(url, this.options).map(res => res.json());
  }

  getUser() {
    return this.user;
  }

  setUser(user: User) {
    this.user = user;
  }

  getAllByLtiResourceLink() {
    const url: string = `${this.apiUrl}`;
    return this.http.get(url, this.options).map(res=>res.json())
  }
}
