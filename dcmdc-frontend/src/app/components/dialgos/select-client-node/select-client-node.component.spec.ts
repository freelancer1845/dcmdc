import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectClientNodeComponent } from './select-client-node.component';

describe('SelectClientNodeComponent', () => {
  let component: SelectClientNodeComponent;
  let fixture: ComponentFixture<SelectClientNodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectClientNodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectClientNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
