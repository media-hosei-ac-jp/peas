import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { environment } from '../environments/environment';

@Injectable()
export class ResultService {
  private apiUrl = environment.beUrl + 'api/result';
  private headers = new Headers({ 'Content-Type': 'application/json' });
  private options = new RequestOptions({ headers: this.headers, withCredentials: true });

  constructor(private http: Http) { }

  getAllResultInfo() {
    const url: string = `${this.apiUrl}/getAllResultInfo`;
    let params: URLSearchParams = new URLSearchParams();
    return this.http.get(url, this.options).map(res=>res.json())
  }

  get(targetId) {
    const url: string = `${this.apiUrl}/${targetId}`;
    let params: URLSearchParams = new URLSearchParams();
    return this.http.get(url, this.options).map(this.extractData)
  }

  getAverage(targetId) {
    const url: string = `${this.apiUrl}/getAverageByTargetId/${targetId}`;
    let params: URLSearchParams = new URLSearchParams();
    return this.http.get(url, this.options).map(this.extractData)
  }

  private extractData(res: Response) {
   let body;
   if(res.text()) {
     body = res.json();
   }
   return body || {};
  }
}
