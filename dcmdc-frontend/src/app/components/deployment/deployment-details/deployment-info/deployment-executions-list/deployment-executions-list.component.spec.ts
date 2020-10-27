import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeploymentExecutionsListComponent } from './deployment-executions-list.component';

describe('DeploymentExecutionsListComponent', () => {
  let component: DeploymentExecutionsListComponent;
  let fixture: ComponentFixture<DeploymentExecutionsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeploymentExecutionsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeploymentExecutionsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
