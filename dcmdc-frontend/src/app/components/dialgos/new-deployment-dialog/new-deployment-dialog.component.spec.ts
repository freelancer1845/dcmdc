import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDeploymentDialogComponent } from './new-deployment-dialog.component';

describe('NewDeploymentDialogComponent', () => {
  let component: NewDeploymentDialogComponent;
  let fixture: ComponentFixture<NewDeploymentDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewDeploymentDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewDeploymentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
