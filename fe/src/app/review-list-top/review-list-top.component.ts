import { Component, OnInit } from '@angular/core';
import { QuizService } from '../quiz.service';

@Component({
  selector: 'app-review-list-top',
  templateUrl: './review-list-top.component.html',
  styleUrls: ['./review-list-top.component.css']
})
export class ReviewListTopComponent implements OnInit {
  quizzes;

  constructor(private quizService: QuizService) {
  }

  ngOnInit() {
    this.getQuizzes();
  }

  getQuizzes() {
    this.quizService.getAll().subscribe(res=> {
      this.quizzes = res;
      this.quizzes = this.quizzes.filter(x=>!x.deleted);
      this.quizzes.sort((q1, q2)=>q2.created-q1.created);
    });
  }

}
