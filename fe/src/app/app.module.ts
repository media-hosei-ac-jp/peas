import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { TopComponent } from './top/top.component';

import { RouterModule, Routes } from '@angular/router';
import { AdminTopComponent } from './admin-top/admin-top.component';
import { ResultComponent } from './result/result.component';
import { ReviewComponent } from './review/review.component';
import { EditQuizComponent } from './edit-quiz/edit-quiz.component';
import { ResultTopComponent } from './result-top/result-top.component';
import { QuizComponent } from './quiz/quiz.component';
import { EditTargetComponent } from './edit-target/edit-target.component';

import { QuizService } from './quiz.service';
import { UserService } from './user.service';
import { TargetService } from './target.service';
import { ReviewService } from './review.service';
import { ResultService } from './result.service';
import { WebSocketService } from './web-socket.service';
import { AdminService } from './admin.service';

//import { RatingModule } from "ngx-rating";
import { ReviewTopComponent } from './review-top/review-top.component';
import { PapaParseModule } from 'ngx-papaparse';
import { ReviewListComponent } from './review-list/review-list.component';
import { ReviewListTopComponent } from './review-list-top/review-list-top.component';
import { EditGroupComponent } from './edit-group/edit-group.component';

import {NgxChartsModule} from '@swimlane/ngx-charts';
//import {AnimationsModule} from '@angular/animations';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { RatingComponent } from './rating/rating.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { ReviewGuestComponent } from './review-guest/review-guest.component';

export const appRoutes: Routes = [
  { path: 'top', component: TopComponent },
  { path: 'admin-top', component: AdminTopComponent },
  { path: 'review/:id', component: ReviewComponent },
  { path: 'review-top', component: ReviewTopComponent },
  { path: 'result-top', component: ResultTopComponent },
  { path: 'result/:id', component: ResultComponent},
  { path: 'review-list-top', component: ReviewListTopComponent },
  { path: 'review-list/:id', component: ReviewListComponent },
  { path: 'edit-quiz', component: EditQuizComponent },
  { path: 'edit-quiz/:id', component: EditQuizComponent },
  { path: 'quiz/:id', component: QuizComponent },
  { path: 'quiz/:id/debug', component: QuizComponent, data: {debug: true}},
  { path: 'edit-target/:id', component: EditTargetComponent },
  { path: 'create-group/:qid', component: EditGroupComponent },
  { path: 'edit-group/:tid', component: EditGroupComponent },
  { path: 'admin-login', component: AdminLoginComponent },
  { path: 'review-guest/:id', component: ReviewGuestComponent },
  { path: '', redirectTo: 'top', pathMatch: 'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    TopComponent,
    AdminTopComponent,
    ResultComponent,
    ReviewComponent,
    EditQuizComponent,
    ResultTopComponent,
    QuizComponent,
    EditTargetComponent,
    ReviewTopComponent,
    ReviewListComponent,
    ReviewListTopComponent,
    EditGroupComponent,
    RatingComponent,
    AdminLoginComponent,
    ReviewGuestComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes, { useHash: true }),
//    RatingModule,
    PapaParseModule,
    NgxChartsModule, BrowserAnimationsModule
  ],
  providers: [
    QuizService,
    UserService,
    TargetService,
    ReviewService,
    ResultService,
    WebSocketService,
    AdminService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
