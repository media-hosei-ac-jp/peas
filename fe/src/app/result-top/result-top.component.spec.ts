import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultTopComponent } from './result-top.component';

describe('ResultTopComponent', () => {
  let component: ResultTopComponent;
  let fixture: ComponentFixture<ResultTopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResultTopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResultTopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
