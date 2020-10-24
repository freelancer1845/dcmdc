import { Component } from '@angular/core';
import { NavLink } from '@components/navigation/navigation.component';
import { ClientsService } from '@services/clients.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'dcmdc-frontend';
  clients = undefined;

  links: NavLink[] = [
    {
      label: "Dashboard",
      route: "pages/dashboard",
    }
  ]

  constructor(private clientService: ClientsService) {
    clientService.getClients().subscribe(c => this.clients = c);
  }
}
