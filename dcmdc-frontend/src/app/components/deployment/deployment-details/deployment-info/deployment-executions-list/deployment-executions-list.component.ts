import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DeploymentExecution } from '@model/deployment.model';
import { DeploymentsService } from '@services/deployments.service';
import { EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

@Component({
  selector: 'app-deployment-executions-list',
  templateUrl: './deployment-executions-list.component.html',
  styleUrls: ['./deployment-executions-list.component.css']
})
export class DeploymentExecutionsListComponent implements OnInit {
  displayedColumns: string[] = ['target', 'state', 'error'];
  dataSource: DeploymentExecution[];

  constructor(private service: DeploymentsService, private activatedRoute: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(flatMap(params => {
      if (params['id'] != undefined) {
        return this.service.getDeploymentExecutionsPage(params['id'], 0, 10);
      } else {
        return EMPTY;
      }
    })).subscribe(d => this.dataSource = d.content);

  }

}
