import { Component, OnInit } from '@angular/core';
import { Quiz } from '../quiz';
import { QuizItem } from '../quiz-item';
import { QuizService } from '../quiz.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ResultService } from '../result.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { ReviewService } from '../review.service';
import { WebSocketService } from '../web-socket.service';
import { Target } from '../target';
import { TargetService } from '../target.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {
  quiz: Quiz;
  result;
  user: User;
  hasRole = User.hasRole;
  sending: boolean = false;
  id: number;
  target: Target;

  averageResult;

  //charts
  /*
  multi = [
  {
    "name": "Germany",
    "series": [
      {
        "name": "2010",
        "value": 7300000
      },
      {
        "name": "2011",
        "value": 8940000
      }
    ]
  },

  {
    "name": "USA",
    "series": [
      {
        "name": "2010",
        "value": 7870000
      },
      {
        "name": "2011",
        "value": 8270000
      }
    ]
  },

  {
    "name": "France",
    "series": [
      {
        "name": "2010",
        "value": 5000002
      },
      {
        "name": "2011",
        "value": 5800000
      }
    ]
  }
];
*/
  multi = [];

  //view: any[] = [700, 400];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = '設問'; //ja
//  xAxisLabel = 'question'; //ja
  showYAxisLabel = true;
  yAxisLabel = '得点'; //ja
//  yAxisLabel = 'point'; //ja

  colorScheme = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };

  // line, area
  //autoScale = false; //trueだと最小値をyScaleMinに自動設定
  autoScale = true;
  //yScaleMin: number = 1;
  //yScaleMax: number = 5;

  constructor(private route: ActivatedRoute,
    private quizService: QuizService,
    private resultService: ResultService,
    private userService: UserService,
    private reviewService: ReviewService,
    private wsService: WebSocketService,
    private targetService: TargetService) {
    this.id = route.snapshot.params['id'];
    if(this.id) {
      this.getResult(this.id);
      this.getTarget(this.id);
    }
    this.getUser();
    this.subscribeWS();
  }

  ngOnInit() {
  }

  getUser() {
    this.userService.me().subscribe(res=>this.user = res);
  }

  getTarget(id) {
    this.targetService.get(id).subscribe(res=>{
      //console.log(res);
      this.target = res;
    });
  }

  getResult(id) {
    this.resultService.get(id).subscribe(
      res=>{
        this.result = res;

        //for charts
        //cale yScaleMax
        /*
        let max = 0;
        for(let i=0; i<res.items.length; i++) {
          let item = res.items[i];
          if(item.quizItem.type=='Score') {
            let t = item.quizItem.maxScore;
            max = max>t?max:t;
          }
        }
        this.yScaleMax = max;
        */

        let series = [];
        for(let i=0; i<res.items.length; i++) {
          let item = res.items[i];
          if(item.quizItem.type=='Score') {
            let name = 'Q'+(i+1);
            let value = item.averageScore/100;
            series.push({ "name": name, "value": value});
          }
        }
        let tMulti = [];
        tMulti[0] = {
          name: '自分の得点',
          series: series
        };


        //sort comments
        this.result.items.forEach(ri=>{
           if(ri.comments) {
             ri.comments.sort((c1, c2)=>{
               let b1 = this.hasRole(c1, 'Instructor');
               let b2 = this.hasRole(c2, 'Instructor');
               if(b1&&b2) {
                 return 0;
               }
               if(b1) {
                 return -1;
               }
               return 1;
             });
           }

           if(!ri.comments) {
             return;
           }
           let count = 0;
           for(let i=0; i<ri.comments.length; i++) {
             if(ri.comments[i].content &&
               (this.hasRole(this.user, 'Instructor') || !ri.comments[i].hidden)) {
               count++;
             }
           }
           ri.count = count;
           //console.log(ri);
        });

        this.getAverageResult(this.id, tMulti);
      }
    );
  }

  getAverageResult(id, tMulti) {
    this.resultService.getAverage(id).subscribe(
      res=>{
        console.log(res);
        this.averageResult = res;

        let series = [];
        for(let i=0; i<res.items.length; i++) {
          let item = res.items[i];
          if(item.quizItem.type=='Score') {
            let name = 'Q'+(i+1);
            let value = item.averageScore/100;
            series.push({ "name": name, "value": value});
          }
        }
        tMulti[1] = {
          name: 'クラスの平均点',
          series: series
        };

        this.multi = tMulti;
      }
    );
  }

  hasRoleC(comment, role: string) {
    return comment.roles.some(r=>r==role);
  }

  setHidden(comment, value) {
    this.sending = true;
    this.reviewService.setReviewItemHidden(comment.reviewItemId, value).subscribe(res=>{
      this.sending = false;
      comment.hidden = value;
    });

  }

  subscribeWS() {
    this.wsService.getDataStream().subscribe(res=>{
      let data = JSON.parse(res.data);
      if(data.msg=='ReviewSubmitted') {
        this.getResult(this.id);
      }
    });
  }

  setResultPublished(published) {
    this.target.resultPublished = published;
    this.sending = true;
    this.targetService.save(this.target).subscribe(res=>{
      this.sending = false;
      console.log('update completed');
    });
  }

}
