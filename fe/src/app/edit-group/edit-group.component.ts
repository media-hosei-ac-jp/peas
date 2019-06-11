import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Target } from '../target';
import { Quiz } from '../quiz';
import { TargetService } from '../target.service';
import { User } from '../user';
import { UserService } from '../user.service';
import {Location} from '@angular/common';

@Component({
  selector: 'app-edit-group',
  templateUrl: './edit-group.component.html',
  styleUrls: ['./edit-group.component.css']
})
export class EditGroupComponent implements OnInit {
  qid: number;
  target: Target;
  //targets: Target[];
  //users: User[];
  users: any[];
  sending: boolean;

  constructor(private route: ActivatedRoute,
    private targetService: TargetService,
    private userService: UserService,
    public location: Location) {
  }

  ngOnInit() {
    let qid = this.route.snapshot.params['qid'];
    let tid = this.route.snapshot.params['tid'];

    //create group
    if(qid) {
      this.qid = qid;
      this.getUsers();

      this.target = new Target;
      this.target.quiz = new Quiz;
      this.target.quiz.id = qid;
      this.target.active = true;
    }
    else if(tid) { //edit group
      this.getTarget(tid);
    }
  }

  getUsers() {
    this.userService.getAllByLtiResourceLink().subscribe(
      res=>{
        this.users = res;
        this.checkUsers();
      }
    );
  }

  checkUsers() {
    this.users.forEach(u=>{
      u.checked = this.target.users.some(u2=>u2.id==u.id);
    });
  }

  getTarget(id) {
    this.targetService.get(id).subscribe(
      res=>{
        //console.log(res);
        this.target = res;
        this.getUsers();
      }
    );
  }


  save() {
    //console.log(this.target);
    this.sending = true;
    this.target.users = [];
    this.users.forEach(u=>{
      if(u.checked) {
        this.target.users.push(u);
      }
    });
    if(this.qid) { //create
      this.targetService.create(this.qid, this.target).subscribe(
        res=>{
          this.target = res;
          this.sort(this.users);
          alert('グループを保存しました。')
          this.sending = false;
        }
      );
    }
    else { //edit
      this.targetService.save(this.target).subscribe(
        res=>{
          this.target = res;
          this.sort(this.users);
          alert('グループを保存しました。')
          this.sending = false;
        }
      );
    }
  }

  sort(users) {
    users.sort((t1, t2)=>{
      if(t1.checked && !t2.checked) return -1;
      if(!t1.checked && t2.checked) return 1;
      return t1.id-t2.id;
    });
  }

  checkAll(b) {
    this.users.forEach((e: any)=>e.checked=b);
  }
}
