import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePortBindingDialogComponent } from './create-port-binding-dialog.component';

describe('CreatePortBindingDialogComponent', () => {
  let component: CreatePortBindingDialogComponent;
  let fixture: ComponentFixture<CreatePortBindingDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreatePortBindingDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatePortBindingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
