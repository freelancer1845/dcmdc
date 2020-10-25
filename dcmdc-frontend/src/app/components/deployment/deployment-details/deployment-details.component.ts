import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Deployment } from '@model/deployment.model';
import { DeploymentsService } from '@services/deployments.service';
import { EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

@Component({
  selector: 'app-deployment-details',
  templateUrl: './deployment-details.component.html',
  styleUrls: ['./deployment-details.component.css']
})
export class DeploymentDetailsComponent implements OnInit {

  deployment: Deployment;

  constructor(private service: DeploymentsService, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(flatMap(params => {
      if (params['id'] != undefined) {
        return this.service.getById(params['id']);
      } else {
        return EMPTY;
      }
    })).subscribe(d => this.deployment = d);
  }

}
