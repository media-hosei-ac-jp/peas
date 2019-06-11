import { Injectable } from '@angular/core';
import { Quiz } from './quiz';

import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { environment } from '../environments/environment';

@Injectable()
export class AdminService {
  private apiUrl = environment.beUrl + 'api/admin';
  private headers = new Headers({ 'Content-Type': 'application/json' });
  private options = new RequestOptions({ headers: this.headers, withCredentials: true });

  constructor(private http: Http) { }

  login4A(id: number) {
    const url: string = `${this.apiUrl}/login4A`;
    let options = this.options.merge({
      params: {uid: id}
    });
    return this.http.get(url, options).map(res=>res.json());
  }

  login4I(id: number) {
    const url: string = `${this.apiUrl}/login4I`;
    let options = this.options.merge({
      params: {uid: id}
    });
    return this.http.get(url, options).map(res=>res.json());
  }
}
