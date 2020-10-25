import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ClientNode } from '@model/client.model';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

export const EXAMPLE_CLIENTS: ClientNode[] = [
  {
    api_id: "5CCE13B7A841148BA5DF86227C2B086ECE4C0E592ACAC46A6367B6CAA5F29AB3",
    api_secret: "example_secret",
    name: "Example Client",
    description: "Example Test Client"
  }
]

@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  constructor(private http: HttpClient) { }

  public getClients(): Observable<ClientNode[]> {
    return of(EXAMPLE_CLIENTS);
    // return this.http.get<ClientNode[]>(environment.host + "api/v1/clients");
  }
}
