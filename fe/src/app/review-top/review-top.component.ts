import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { TargetService } from '../target.service';
import { Target } from '../target';
import { WebSocketService } from '../web-socket.service';

@Component({
  selector: 'app-review-top',
  templateUrl: './review-top.component.html',
  styleUrls: ['./review-top.component.css']
})
export class ReviewTopComponent implements OnInit {
  target: Target;

  constructor(private targetService: TargetService,
    private router: Router,
    private location: Location,
    private wsService: WebSocketService) {
    this.getTarget();
    this.subscribeWS();
  }

  ngOnInit() {
  }

  getTarget() {
    this.targetService.getTargetedTarget().subscribe(
      res=>{
        this.target = res;
        if(this.location.path() == '/review-top' && this.target.id) {
          this.router.navigate(['review/'+this.target.id]);
        }
      }
    );
  }

  subscribeWS() {
    this.wsService.getDataStream().subscribe(res=>{
      let data = JSON.parse(res.data);
      console.log(data);
      if(data.msg=='UpdateTarget') {
        this.getTarget();
      }
    });
  }

}
