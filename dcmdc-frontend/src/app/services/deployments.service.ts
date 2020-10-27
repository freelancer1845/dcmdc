import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Deployment, DeploymentExecution } from '../model/deployment.model';
import { EXAMPLE_CLIENTS } from './clients.service';
import { Page } from '@model/spring-boot.model';

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

  constructor(private http: HttpClient) { }


  public getAll(): Observable<Deployment[]> {
    // return of(EXAMPLE_DEPLOYMENTS);
    return this.http.get<Deployment[]>(environment.host + 'api/v1/deployments').pipe(map(values => values.map(this.decodeConfiguration)));
  }

  public getById(id: number): Observable<Deployment> {
    return this.http.get<Deployment>(environment.host + 'api/v1/deployments/' + id).pipe(map(this.decodeConfiguration));
  }

  public getDeploymentExecutionsPage(id: number, index: number, size: number): Observable<Page<DeploymentExecution>> {
    return this.http.post<Page<DeploymentExecution>>(environment.host + 'api/v1/deployments/executions/' + id, {
      page: index,
      pageSize: size
    });
  }

  public patch(deployment: Deployment): Observable<Deployment> {
    return this.http.patch<Deployment>(environment.host + 'api/v1/deployments', this.encodeConfiguration(deployment)).pipe(map(this.decodeConfiguration));
  }

  public create(deployment: Partial<Deployment>): Observable<Deployment> {
    return this.http.post<Deployment>(environment.host + 'api/v1/deployments', this.encodeConfiguration(deployment)).pipe(map(this.decodeConfiguration));
  }

  public delete(deploymentId: number): Observable<any> {
    return this.http.delete(environment.host + 'api/v1/deployments/' + deploymentId);
  }

  private decodeConfiguration(d: Deployment): Deployment {
    d.configuration = atob(d.configuration);
    return d;
  }

  private encodeConfiguration(d: Partial<Deployment>): Partial<Deployment> {
    if (d.configuration) {
      d.configuration = btoa(d.configuration);
    }
    return d;
  }
}
