import { Component, OnInit } from '@angular/core';
import { Quiz } from '../quiz';
import { QuizItem } from '../quiz-item';
import { QuizService } from '../quiz.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Target } from '../target';
import { TargetService } from '../target.service';
import { Review } from '../review';
import { ReviewItem } from '../review-item';
import { ReviewService } from '../review.service';
import { WebSocketService } from '../web-socket.service';

@Component({
  selector: 'app-review-guest',
  templateUrl: './review-guest.component.html',
  styleUrls: ['./review-guest.component.css']
})
export class ReviewGuestComponent implements OnInit {
  quiz: Quiz;
  target: Target;
  review: Review = new Review;
  sending: boolean = false;
  editing: boolean = false;

  targetedTarget: Target;
  inputCompleted: boolean = false;

  constructor(private route: ActivatedRoute,
    private quizService: QuizService,
    private targetService: TargetService,
    private reviewService: ReviewService,
    private wsService: WebSocketService) {
    let id = route.snapshot.params['id'];
    if(id) {
      this.getTarget(id);
    }
    //this.getTargetedTarget();
    this.subscribeWS();

  }

  ngOnInit() {
  }

  getQuizByTargetId(id) {
    this.quizService.getByTargetId4Guest(id).subscribe(
      res=>{
        this.quiz = res;
        //this.getReview();
        this.editing = true;
        this.quiz.items.forEach(item=> {
          this.review.items.push(new ReviewItem(item));
        });
      }
    );
  }

  getTarget(id) {
    this.targetService.get4Guest(id).subscribe(
      res=>{
        this.target = res;
        this.getQuizByTargetId(this.target.id);
      }
    );
  }

  onSubmit() {
    this.saveReview();
  }

  saveReview() {
    this.sending = true;
    this.reviewService.save4Guest(this.target.id, this.review).subscribe(res=>{
      this.review = res;
      this.sending = false;
      this.editing = false;
    });
  }

/*
  getReview() {
    this.reviewService.getByTargetId(this.target.id).subscribe(res=>{
      if(!res.id) {
        this.editing = true;
        this.quiz.items.forEach(item=> {
          this.review.items.push(new ReviewItem(item));
        });
      }
      else {
        this.review = res;
        let items = [];
        this.quiz.items.forEach(item=> {
          let ri = null;
          for(let i=0; i<res.items.length;i++) {
            let tri = res.items[i];
            if(tri.quizItem.id == item.id) {
              ri = tri;
              break;
            }
          }
          if(ri==null) {
            ri = new ReviewItem(item);
          }
          items.push(ri);
        });
        this.review.items = items;
        this.checkInputs();
      }
    });
  }
*/
/*
  getTargetedTarget() {
    this.targetService.getTargetedTarget().subscribe(
      res=>{
        this.targetedTarget = res;
      }
    );
  }
  */

  checkInputs() {
    this.inputCompleted = this._inputCompleted();
  }

  _inputCompleted() {
    for(let i=0; i<this.quiz.items.length; i++) {
      let qi = this.quiz.items[i];
      let ri = this.review.items[i];
      if(qi.typeStr=='Score' && ri.score==0) {
        return false;
      }
    }
    return true;
  }

  subscribeWS() {
    this.wsService.getDataStream().subscribe(res=>{
      let data = JSON.parse(res.data);
      console.log(data);
      /*
      if(data.msg=='UpdateTarget') {
        this.getTargetedTarget();
      }
      */
    });
  }
}
