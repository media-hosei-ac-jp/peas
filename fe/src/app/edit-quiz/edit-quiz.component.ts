import { Component, OnInit } from '@angular/core';
import { Quiz } from '../quiz';
import { QuizItem } from '../quiz-item';
import { QuizService } from '../quiz.service';
import { Router, ActivatedRoute } from '@angular/router';
import 'rxjs/add/operator/switchMap';

import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-edit-quiz',
  templateUrl: './edit-quiz.component.html',
  styleUrls: ['./edit-quiz.component.css']
})
export class EditQuizComponent implements OnInit {
  quiz: Quiz = new Quiz;
  sending: boolean;
  error;
  preview: boolean;
  saved: boolean;
  scores: number[] = [];

  fileKeyMap = {
      'Score': '得点',
      'Free': '自由記述'
  };
  fileData;

  constructor(private quizService: QuizService,
    private route: ActivatedRoute,
    private router: Router) {
    let id = route.snapshot.params['id'];
    if(id) {
      this.getQuiz(id);
    }

  }

  ngOnInit() {
  }

  swapItems(i, j) {
    let t = this.quiz.items[i];
    this.quiz.items[i] = this.quiz.items[j];
    this.quiz.items[j] = t;
  }

  addQuizItem() {
    let item = new QuizItem('Score', 2);
    //item.type = 'Score';
    //item.maxScore = 1;
    this.quiz.items.push(item);
  }

  saveQuiz() {
    //console.log(this.quiz);
    this.sending = true;
    this.error = undefined;
    this.saved = false;
    this.quizService.save(this.quiz).subscribe(
      res=>{
        console.log('saved');
        this.quiz = res;
        this.sending = false;
        this.saved = true;
      },
      err=>{this.error = err;}
    );
  }

  getQuiz(id) {
    //console.log(this.quiz);
    this.quizService.get(id).subscribe(
      res=>{
        this.quiz = res;
      }
    );
  }

  deleteQuiz() {
    this.quiz.deleted = true;
    this.quizService.save(this.quiz).subscribe(
      res=>{
        this.router.navigate(['admin-top'])
      }
    );
  }

  saveFile() {
    let fileName = this.quiz.title;
    let data = [];
    let header = ['問題文','問題の種類','最大得点'];
    data.push(header);
    this.quiz.items.forEach(item=>{
      let d = [];
      let maxScore = item.typeStr=='Score'?item.maxScore:'';
      d.push(item.text);
      d.push(this.fileKeyMap[item.typeStr]);
      d.push(maxScore);

      data.push(d);
    });
    this.save(fileName, data);
  }

  s2ab(s) {
    let buf = new ArrayBuffer(s.length);
    let view = new Uint8Array(buf);
    for (let i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
    return buf;
  }

  save(fileName, data) {
    /* generate worksheet */
    let ws: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(data);
    /* generate workbook and add the worksheet */
    let wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    /* save to file */
    const wbout: string = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
    FileSaver.saveAs(new Blob([this.s2ab(wbout)]), fileName+'.xlsx');
  }

  //upload
  /* <input type="file" (change)="onFileChange($event)" multiple="false" /> */
/* ... (within the component class definition) ... */
  onFileChange(evt: any) {
    /* wire up file reader */
    const target: DataTransfer = <DataTransfer>(evt.target);
    if (target.files.length !== 1) throw new Error('Cannot use multiple files');
    const reader: FileReader = new FileReader();
    reader.onload = (e: any) => {
      /* read workbook */
      const bstr: string = e.target.result;
      const wb: XLSX.WorkBook = XLSX.read(bstr, {type: 'binary'});

      /* grab first sheet */
      const wsname: string = wb.SheetNames[0];
      const ws: XLSX.WorkSheet = wb.Sheets[wsname];

      /* save data */
      //this.fileData = <AOA>(XLSX.utils.sheet_to_json(ws, {header: 1}));
      this.fileData = (XLSX.utils.sheet_to_json(ws, {header: 1}));
    };
    reader.readAsBinaryString(target.files[0]);
  }

  loadFile() {
    console.log(this.fileData);
    let items = [];
    let map = this.inverseObject(this.fileKeyMap, false);

    for(let i=1; i<this.fileData.length; i++) {
      let item = this.fileData[i];
      let ri = {
        text: item[0],
        typeStr: map[item[1]],
        maxScore: item[2]
      };
      items.push(ri);
    }

    this.quiz.items = items;
  }

  /**
   * オブジェクトのkeyとvalueを反転させる
   * @param {Object} obj 反転させるオブジェクト
   * @param {Boolean} [keyIsNumber=false] keyが数値であるか？（数値ならkeyを反転させるとき数値に変換する）
   * @return {Object} keyとvalueの反転したオブジェクト
   */
   inverseObject (obj, keyIsNumber) {
    return Object.keys(obj).reduceRight((ret, k)=>{
      return (ret[obj[k]] = keyIsNumber ? parseInt(k, 10) : k, ret);
    }, {});
  }

  getRows(str, min) {
    let len = str?str.split('\n').length:1;
    return len>min?len:min;
  }
}
