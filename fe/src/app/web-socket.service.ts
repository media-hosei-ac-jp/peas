import { Injectable } from '@angular/core';
import {$WebSocket} from 'angular2-websocket/angular2-websocket';
import { environment } from '../environments/environment';

@Injectable()
export class WebSocketService {
  private wsUrl = environment.wsUrl;
  private ws;

  constructor() {
    let url = this.wsUrl;
    this.ws = new $WebSocket(url);
    console.log('connected');
  }

  getDataStream() {
    return this.ws.getDataStream();
  }

}
