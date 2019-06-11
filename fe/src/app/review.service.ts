import { Injectable } from '@angular/core';
import { Review } from './review';


import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { environment } from '../environments/environment';

@Injectable()
export class ReviewService {
  private apiUrl = environment.beUrl + 'api/review';
  private headers = new Headers({ 'Content-Type': 'application/json' });
  private options = new RequestOptions({ headers: this.headers, withCredentials: true });

  constructor(private http: Http) { }

  getByTargetId(targetId) {
    const url: string = `${this.apiUrl}/getByTargetId/${targetId}`;
    return this.http.get(url, this.options).map(this.extractData);
  }

  save(targetId, review) {
    const url: string = `${this.apiUrl}/${targetId}`;
    return this.http.post(url, review, this.options).map(res => res.json());
  }

  save4Guest(targetId, review) {
    const url: string = `${this.apiUrl}/guest/${targetId}`;
    return this.http.post(url, review, this.options).map(res => res.json());
  }

  saveByInstructor(ownerId, targetId, review) {
    const url: string = `${this.apiUrl}/${ownerId}/${targetId}`;
    return this.http.post(url, review, this.options).map(res => res.json());
  }

  private extractData(res: Response) {
   let body;
   if(res.text()) {
     body = res.json();
   }
   return body || {};
  }

  setReviewItemHidden(id, value) {
    const url: string = `${this.apiUrl}/setReviewItemHidden/${id}`;
    let params: URLSearchParams = new URLSearchParams();
    params.set('value', String(value));
    let options = this.options.merge({search: params});
    return this.http.get(url, options).map(res=>res.json());
  }
}
