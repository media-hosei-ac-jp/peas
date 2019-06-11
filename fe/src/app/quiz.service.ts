import { Injectable } from '@angular/core';
import { Quiz } from './quiz';

import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { environment } from '../environments/environment';

@Injectable()
export class QuizService {
  private apiUrl = environment.beUrl + 'api/quiz';
  private headers = new Headers({ 'Content-Type': 'application/json' });
  private options = new RequestOptions({ headers: this.headers, withCredentials: true });

  constructor(private http: Http) { }

  save(quiz) {
    const url: string = `${this.apiUrl}`;
    return this.http.post(url, quiz, this.options).map(res => res.json());
  }

  get(id: number) {
    const url: string = `${this.apiUrl}/${id}`;
    return this.http.get(url, this  .options).map(res=>res.json());
  }

  getByTargetId(id: number) {
    const url: string = `${this.apiUrl}/getByTargetId/${id}`;
    return this.http.get(url, this  .options).map(res=>res.json());
  }

  getByTargetId4Guest(id: number) {
    const url: string = `${this.apiUrl}/guest/getByTargetId/${id}`;
    return this.http.get(url, this  .options).map(res=>res.json());
  }

  getAll() {
    const url: string = `${this.apiUrl}`;
    return this.http.get(url, this.options).map(res=>res.json());
  }

  getAllProgressData() {
    const url: string = `${this.apiUrl}/getAllProgressData`;
    return this.http.get(url, this.options).map(res=>res.json());
  }
}
