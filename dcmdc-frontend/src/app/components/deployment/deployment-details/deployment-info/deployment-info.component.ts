import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Deployment } from '@model/deployment.model';
import { DeploymentsService } from '@services/deployments.service';
import { EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

@Component({
  selector: 'app-deployment-info',
  templateUrl: './deployment-info.component.html',
  styleUrls: ['./deployment-info.component.css']
})
export class DeploymentInfoComponent implements OnInit {

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
