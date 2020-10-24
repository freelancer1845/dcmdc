import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientNode } from '@model/client.model';
import { ClientsService } from '@services/clients.service';

@Component({
  selector: 'app-client-nodes-list',
  templateUrl: './client-nodes-list.component.html',
  styleUrls: ['./client-nodes-list.component.css']
})
export class ClientNodesListComponent implements OnInit {

  displayedColumns: string[] = ['uuid', 'name'];
  dataSource: ClientNode[];

  constructor(private clientsService: ClientsService, private router: Router, private route: ActivatedRoute) {
    this.clientsService.getClients().subscribe(clients => this.dataSource = clients);
  }

  ngOnInit(): void {
  }

  details(clientNode: ClientNode) {
    console.log("navigation");
    this.router.navigate(['client-node', clientNode.uuid], { relativeTo: this.route.parent })
  }

}
