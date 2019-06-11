import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewTopComponent } from './review-top.component';

describe('ReviewTopComponent', () => {
  let component: ReviewTopComponent;
  let fixture: ComponentFixture<ReviewTopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewTopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewTopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
