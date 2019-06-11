import { Component, OnInit } from '@angular/core';
import { QuizService } from '../quiz.service';

@Component({
  selector: 'app-admin-top',
  templateUrl: './admin-top.component.html',
  styleUrls: ['./admin-top.component.css']
})
export class AdminTopComponent implements OnInit {
  quizzes;
  progressData;

  constructor(private quizService: QuizService) {
    this.getQuizzes();
  }

  ngOnInit() {
  }

  getQuizzes() {
    this.quizService.getAll().subscribe(res=> {
      this.quizzes = res;
      this.quizzes = this.quizzes.filter(x=>!x.deleted);
      this.quizzes.sort((q1, q2)=>q2.created-q1.created);
      this.getProgressData();
    });
  }

  getProgressData() {
    this.quizService.getAllProgressData().subscribe(res=> {
      this.progressData = res;
      for(let key in this.progressData) {
        let v = this.progressData[key];
        v.notStarted = v.count - v.started;
      }
    });
  }

}
