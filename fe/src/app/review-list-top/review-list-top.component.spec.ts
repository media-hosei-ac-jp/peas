import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewListTopComponent } from './review-list-top.component';

describe('ReviewListTopComponent', () => {
  let component: ReviewListTopComponent;
  let fixture: ComponentFixture<ReviewListTopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewListTopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewListTopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
