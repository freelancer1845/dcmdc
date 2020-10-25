import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Deployment } from '@model/deployment.model';
import { DeploymentsService } from '@services/deployments.service';

import { take } from 'rxjs/operators';

@Component({
  selector: 'app-deployments-list',
  templateUrl: './deployments-list.component.html',
  styleUrls: ['./deployments-list.component.css']
})
export class DeploymentsListComponent implements OnInit {

  displayedColumns: string[] = ['name'];
  dataSource: Deployment[];

  constructor(private service: DeploymentsService, private router: Router, private activeRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.service.getAll().pipe(take(1)).subscribe(d => this.dataSource = d);
  }

  details(deployment: Deployment) {
    this.router.navigate(['details/', deployment.id], { relativeTo: this.activeRoute });
  }

}
