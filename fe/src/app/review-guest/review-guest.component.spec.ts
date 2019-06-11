import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewGuestComponent } from './review-guest.component';

describe('ReviewGuestComponent', () => {
  let component: ReviewGuestComponent;
  let fixture: ComponentFixture<ReviewGuestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewGuestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewGuestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
