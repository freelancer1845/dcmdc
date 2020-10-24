import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientNodesListComponent } from './client-nodes-list.component';

describe('ClientNodesListComponent', () => {
  let component: ClientNodesListComponent;
  let fixture: ComponentFixture<ClientNodesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientNodesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientNodesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
