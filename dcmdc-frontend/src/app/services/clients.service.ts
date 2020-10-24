import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientNode } from '@model/client.model';

@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  constructor(private http: HttpClient) { }

  public getClients(): Observable<ClientNode[]> {


  }
}
