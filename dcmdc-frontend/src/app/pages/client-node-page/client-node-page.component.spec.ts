import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientNodePageComponent } from './client-node-page.component';

describe('ClientNodePageComponent', () => {
  let component: ClientNodePageComponent;
  let fixture: ComponentFixture<ClientNodePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientNodePageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientNodePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
