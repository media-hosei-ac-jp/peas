import { Component, OnInit } from '@angular/core';
import { Quiz } from '../quiz';
import { QuizItem } from '../quiz-item';
import { QuizService } from '../quiz.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Target } from '../target';
import { ResultService } from '../result.service';

@Component({
  selector: 'app-result-top',
  templateUrl: './result-top.component.html',
  styleUrls: ['./result-top.component.css']
})
export class ResultTopComponent implements OnInit {
  resultInfo = [];

  constructor(private resultService: ResultService) {
    this.getResultInfo();
  }

  ngOnInit() {
  }

  getResultInfo() {
    this.resultService.getAllResultInfo().subscribe(res=>{
      this.resultInfo = res;
      //console.log(res);
    });
  }

}
