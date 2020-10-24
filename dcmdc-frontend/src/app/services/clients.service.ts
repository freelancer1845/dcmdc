import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientNode } from '@model/client.model';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  constructor(private http: HttpClient) { }

  public getClients(): Observable<ClientNode[]> {
    return this.http.get<ClientNode[]>(environment.host + "api/v1/clients");
  }
}
