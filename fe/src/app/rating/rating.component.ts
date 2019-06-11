import {Component, OnInit, Input, Output, EventEmitter, HostListener, forwardRef, NgModule} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR, NG_VALIDATORS, Validator, AbstractControl} from "@angular/forms";

@Component({
  selector: 'my-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.css'],
  providers: [
    { provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => RatingComponent), multi: true },
    //{ provide: NG_VALIDATORS, useExisting: forwardRef(() => RatingComponent), multi: true },
  ],
})
export class RatingComponent implements OnInit, ControlValueAccessor {
  //fullIcon = "★";
  //emptyIcon = "☆";

  @Input()
  max : number;

  items: number[] = [];

  model: number;

  private onChange: (m: any) => void;
  private onTouched: (m: any) => void;

  constructor() { }

  ngOnInit() {
    for(let i=1; i<=this.max; i++) {
        this.items.push(i);
    }
  }

  rate(val) {
    //console.log(val);
    this.onChange(val);//これでviewにmodelが反映される
    this.model = val;
  }

  // -------------------------------------------------------------------------
  // Implemented from ControlValueAccessor
  // -------------------------------------------------------------------------
  writeValue(value: number): void {
      this.model = value;
  }

  registerOnChange(fn: any): void {
      this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
      this.onTouched = fn;
  }

}
