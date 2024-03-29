import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTopComponent } from './admin-top.component';

describe('AdminTopComponent', () => {
  let component: AdminTopComponent;
  let fixture: ComponentFixture<AdminTopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminTopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminTopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
