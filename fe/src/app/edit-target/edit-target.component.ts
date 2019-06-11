import { Component, OnInit } from '@angular/core';
import { Quiz } from '../quiz';
import { QuizItem } from '../quiz-item';
import { QuizService } from '../quiz.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Target } from '../target';
import { TargetService } from '../target.service';

@Component({
  selector: 'app-edit-target',
  templateUrl: './edit-target.component.html',
  styleUrls: ['./edit-target.component.css']
})
export class EditTargetComponent implements OnInit {
  quiz: Quiz;
  targets: Target[];
  sending: boolean;

  constructor(private route: ActivatedRoute,
    private quizService: QuizService,
    private targetService: TargetService) {
  }

  ngOnInit() {
    let id = this.route.snapshot.params['id'];
    if(id) {
      this.getQuiz(id);
      this.getTargets(id);
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
        this.targets = res;
        this.sort();
      }
    );
  }

  checkAll(b) {
    this.targets.forEach(e=>e.active=b);
  }

  sort() {
    this.targets.sort((t1, t2)=>{
      if(t1.active && !t2.active) return -1;
      if(!t1.active && t2.active) return 1;
      return t1.id-t2.id;
    });
  }

  save() {
    this.sending = true;
    this.targetService.saveAll(this.targets).subscribe(res=>{
      alert('更新しました。');
      this.sending = false;
    });
  }

}
