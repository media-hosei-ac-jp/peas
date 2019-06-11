import { Component, OnInit } from '@angular/core';
import { Quiz } from '../quiz';
import { QuizItem } from '../quiz-item';
import { QuizService } from '../quiz.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Target } from '../target';
import { TargetService } from '../target.service';
import { WebSocketService } from '../web-socket.service';
import { ResultService } from '../result.service';
import { Observable } from 'rxjs';
import { PapaParseService } from 'ngx-papaparse';
import { ReviewService } from '../review.service';
import { Review } from '../review';
import { ReviewItem } from '../review-item';
import { UserService } from '../user.service';
import { User } from '../user';

@Component({
  selector: 'app-review-list',
  templateUrl: './review-list.component.html',
  styleUrls: ['./review-list.component.css']
})
export class ReviewListComponent implements OnInit {
  quiz: Quiz;
  targets: Target[];
  loading: boolean;
  user: User;

  constructor(private route: ActivatedRoute,
    private quizService: QuizService,
    private targetService: TargetService,
    private wsService: WebSocketService,
    private resultService: ResultService,
    private papa: PapaParseService,
    private reviewService: ReviewService,
    private userService: UserService) {
  }

  ngOnInit() {
    let id = this.route.snapshot.params['id'];
    if(id) {
      this.getQuiz(id);
      this.userService.me().subscribe(res=>{
        this.user = res;
        this.getTargets(id);
      });
    }
  }


  getQuiz(id) {
    this.quizService.get(id).subscribe(
      res=>{
        this.quiz = res;
      }
    );
  }

  getTargets(id) {
    this.targetService.getAll(id).subscribe(
      res=>{
        this.targets = res.filter(t=>{
          return t.active && t.users.every(e=>e.id!=this.user.id);//userを含まない
        });
      }
    );
  }

}
