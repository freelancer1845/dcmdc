import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Deployment } from '../model/deployment.model';
import { EXAMPLE_CLIENTS } from './clients.service';

const EXAMPLE_DEPLOYMENTS: Deployment[] = [
  {
    id: 0,
    name: "Hello World Deployment",
    targetNodes: [
      EXAMPLE_CLIENTS[0],
    ],
    containerName: "hello-world-from-service",
    configuration: '{"Image": "hello-world:latest", "HostBindings": { "PortBindings": {"8080/tcp": ["127.0.0.1", "8080"]}}}',

  }, {
    id: 1,
    name: "Nginx Deployment",
    targetNodes: [],
    containerName: "nginx-from-service",
    configuration: '{"Image": "nginx:latest", "HostBindings": { "PortBindings": {"80/tcp": ["127.0.0.1", "80"]}}}',

  },
];

@Injectable({
  providedIn: 'root'
})
export class DeploymentsService {

  constructor() { }


  public getAll(): Observable<Deployment[]> {
    return of(EXAMPLE_DEPLOYMENTS);
  }

  public getById(id: number): Observable<Deployment> {
    return of(EXAMPLE_DEPLOYMENTS.find(d => d.id == id));
  }

}
