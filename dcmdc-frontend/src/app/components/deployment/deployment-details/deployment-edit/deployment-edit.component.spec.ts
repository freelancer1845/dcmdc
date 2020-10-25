import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeploymentEditComponent } from './deployment-edit.component';

describe('DeploymentEditComponent', () => {
  let component: DeploymentEditComponent;
  let fixture: ComponentFixture<DeploymentEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeploymentEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeploymentEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
