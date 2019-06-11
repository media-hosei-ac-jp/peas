import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { environment } from '../environments/environment';
import { Target } from'./target';

@Injectable()
export class TargetService {
  private apiUrl = environment.beUrl + 'api/target';
  private headers = new Headers({ 'Content-Type': 'application/json' });
  private options = new RequestOptions({ headers: this.headers, withCredentials: true });

  constructor(private http: Http) { }

  getAll(quizId: number) {
    const url: string = `${this.apiUrl}`;
    let params: URLSearchParams = new URLSearchParams();
    params.set('quizId', String(quizId));
    let options = this.options.merge({search: params});
    return this.http.get(url, options).map(res=>res.json())
  }

  updateTargeted(target) {
    const url: string = `${this.apiUrl}/updateTargeted`;
    return this.http.post(url, target, this.options).map(res => res.json());
  }

  save(target) {
    const url: string = `${this.apiUrl}`;
    return this.http.post(url, target, this.options).map(res => res.json());
  }

  saveAll(targets) {
    const url: string = `${this.apiUrl}/saveAll`;
    return this.http.post(url, targets, this.options).map(res => res.json());
  }

  create(quizId, target) {
    const url: string = `${this.apiUrl}/create`;
    let params: URLSearchParams = new URLSearchParams();
    params.set('quizId', String(quizId));
    let options = this.options.merge({search: params});
    return this.http.post(url, target, options).map(res=>res.json());
  }

  getAllProgressData(quizId) {
    const url: string = `${this.apiUrl}/getAllProgressData`;
    let params: URLSearchParams = new URLSearchParams();
    params.set('quizId', String(quizId));
    let options = this.options.merge({search: params});
    return this.http.get(url, options).map(res=>res.json());
  }

  getTargetedProgressData() {
    const url: string = `${this.apiUrl}/getTargetedProgressData`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  private extractData(res: Response) {
   let body;
   if(res.text()) {
     body = res.json();
   }
   return body || {};
  }

  getTargetedTarget() {
    const url: string = `${this.apiUrl}/getTargetedTarget`;
    return this.http.get(url, this.options).map(this.extractData);
  }

  get(id) {
    const url: string = `${this.apiUrl}/${id}`;
    return this.http.get(url, this.options).map(this.extractData);
  }

  get4Guest(id) {
    const url: string = `${this.apiUrl}/guest/${id}`;
    return this.http.get(url, this.options).map(this.extractData);
  }
}
