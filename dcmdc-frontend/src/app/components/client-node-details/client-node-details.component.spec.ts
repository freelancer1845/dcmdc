import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientNodeDetailsComponent } from './client-node-details.component';

describe('ClientNodeDetailsComponent', () => {
  let component: ClientNodeDetailsComponent;
  let fixture: ComponentFixture<ClientNodeDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientNodeDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientNodeDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
