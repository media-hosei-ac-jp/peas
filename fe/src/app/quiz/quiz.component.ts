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

@Component({
  selector: 'app-quiz',
  templateUrl: './quiz.component.html',
  styleUrls: ['./quiz.component.css']
})
export class QuizComponent implements OnInit {
  quiz: Quiz;
  targets: Target[];
  loading: boolean;
  progressData;

  debug: boolean = true;
  csvData;
  sending: boolean = false;

  constructor(private route: ActivatedRoute,
    private quizService: QuizService,
    private targetService: TargetService,
    private wsService: WebSocketService,
    private resultService: ResultService,
    private papa: PapaParseService,
    private reviewService: ReviewService) {
    this.debug = route.snapshot.data['debug'];

    let id = route.snapshot.params['id'];
    if(id) {
      this.getQuiz(id);
      this.getTargets(id);
    }
    this.subscribeWS();
  }

  ngOnInit() {
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
        this.targets = res.filter(t=>t.active);
        this.getProgressData(id);
      }
    );
  }

  sortTargets() {
    this.targets.sort((t1,t2)=> {
      if(t1.targeted) {
        return -1;
      }
      if(t2.targeted) {
        return 1;
      }
      // 投稿数が0を優先して上に
      let sc1 = this.progressData[t1.id].submitted;
      let sc2 = this.progressData[t2.id].submitted;
      if(sc1!=sc2) {
        if(sc1==0) {
          return -1;
        }
        if(sc2==0) {
          return 1;
        }
      }
      // nameの自書式ソート
      return t1.displayName.localeCompare(t2.displayName);
      //return t1.users[0].familyName.localeCompare(t2.users[0].familyName);

    });
  }

  setResultPublished(published) {
    this.loading = true;
    this.quiz.resultPublished = published;
    this.quizService.save(this.quiz).subscribe(res=>{
      this.quiz = res;
      this.loading = false;
    });
  }

  setGuestReview(guestReview) {
    this.loading = true;
    this.quiz.guestReview = guestReview;
    this.quizService.save(this.quiz).subscribe(res=>{
      this.quiz = res;
      this.loading = false;
    });
  }

  setTargetSelected(target, b) {
    this.loading = true;
    if(b) {
      this.targets.forEach(t=>t.targeted=false);
    }
    target.targeted = b;
    this.targetService.updateTargeted(target).subscribe(res=>{
      this.loading = false;
      this.sortTargets();
    });
  }

  getProgressData(quizId) {
    this.targetService.getAllProgressData(quizId).subscribe(res=> {
      this.progressData = res;

      for(let key in this.progressData) {
        let v = this.progressData[key];
        v.notSubmitted = v.count - v.submitted;
      }
      this.sortTargets();
    });
  }

  getTargetedProgressData() {
    this.targetService.getTargetedProgressData().subscribe(res=> {
      let progressData = res;

      for(let key in progressData) {
        let v = progressData[key];
        v.notSubmitted = v.count - v.submitted;
        this.progressData[key] = v;
      }
      this.sortTargets();
    });
  }

  subscribeWS() {
    this.wsService.getDataStream().subscribe(res=>{
      let data = JSON.parse(res.data);
      if(data.msg=='ReviewSubmitted') {
        this.getTargetedProgressData();
      }
    });
  }

  downloadCSV() {
    let str = 'name';

    //console.log(this.quiz);
    for(let i=0; i<this.quiz.items.length; i++) {
      if(this.quiz.items[i].typeStr == 'Score') {
        str += ','+'Q'+(i+1);
      }
    }
    str += '\n';

    let reqs = [];
    for(let i=0; i<this.targets.length; i++) {
      reqs.push(this.resultService.get(this.targets[i].id));
      //this.resultService.get(this.targets[i].id).subscribe(res=>console.log(res));
    }

    Observable.forkJoin(reqs)
    //.subscribe(res => {console.log(res)})
    .subscribe(res => {
      for(let i=0; i<res.length; i++) {
        if(!res[i]['title']) {
          continue;
        }
        str += res[i]['displayName'];
        for(let j=0; j<res[i]['items'].length; j++) {
          if(this.quiz.items[j].typeStr == 'Score') {
            str += ','+(res[i]['items'][j].averageScore/100);
          }
        }
        str += '\n';
      }
      //console.log(str);
      this.downloadFile(this.quiz.title+".csv", str);
    });
  }

  downloadFile(fileName, data: any) {
    let bom = new Uint8Array([0xEF, 0xBB, 0xBF]); //for utf8 dom
    //let parsedResponse = data.text();
    let parsedResponse = data;
    let blob = new Blob([bom, parsedResponse], { type: 'text/csv' });
    let url = window.URL.createObjectURL(blob);

    if(navigator.msSaveOrOpenBlob) {
        navigator.msSaveBlob(blob, fileName);
    } else {
        let a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    }
    window.URL.revokeObjectURL(url);
  }

  //upload csv
  fileOnChange(event, varName) {
    this.readFile(event, (re: any) => {
      this[varName] = re.target.result;
    });
  }

  readFile(event, callback) {
    let files = event.srcElement.files;
    //console.log(files[0]);
    let reader = new FileReader();
    reader.onload = callback;
    reader.readAsText(files[0], 'SJIS');
  }

  addReviews() {
    this.sending = true;
    this.papa.parse(this.csvData, {
      complete: (results, file) => {
        //console.log(results);
        for(let i=1; i<results.data.length; i++) {
          let data = results.data[i];
          let ownerId = data[0];
          let targetId = data[1];
          let review: Review = new Review;
          for(let j=0; j<this.quiz.items.length; j++) {
            let item = this.quiz.items[j];
            review.items.push(new ReviewItem(item));
            if(item.typeStr=='Score') {
              review.items[j].score = data[2+j];
            }
            else {
              if(data[2+j]) {
                review.items[j].content = data[2+j];
              }
            }
          }

          console.log(review);
          this.reviewService.saveByInstructor(ownerId, targetId, review).subscribe(res=>{
            this.sending = false;
          },err=>{
            this.sending = false;
          });
        }
      }
    });
  }

}
