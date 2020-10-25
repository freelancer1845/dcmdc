import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientNode } from '@model/client.model';
import { ClientsService } from '@services/clients.service';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-client-nodes-list',
  templateUrl: './client-nodes-list.component.html',
  styleUrls: ['./client-nodes-list.component.css']
})
export class ClientNodesListComponent implements OnInit {

  displayedColumns: string[] = ['api_id', 'name'];
  dataSource: ClientNode[];

  constructor(private clientsService: ClientsService, private router: Router, private route: ActivatedRoute) {
    this.clientsService.getClients().pipe(take(1)).subscribe(clients => this.dataSource = clients);
  }

  ngOnInit(): void {
  }

  details(clientNode: ClientNode) {
    console.log("navigation");
    this.router.navigate(['client-node', clientNode.api_id], { relativeTo: this.route.parent })
  }

}
