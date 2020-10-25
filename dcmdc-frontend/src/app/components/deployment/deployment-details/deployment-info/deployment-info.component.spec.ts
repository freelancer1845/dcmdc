import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeploymentInfoComponent } from './deployment-info.component';

describe('DeploymentInfoComponent', () => {
  let component: DeploymentInfoComponent;
  let fixture: ComponentFixture<DeploymentInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeploymentInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeploymentInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
