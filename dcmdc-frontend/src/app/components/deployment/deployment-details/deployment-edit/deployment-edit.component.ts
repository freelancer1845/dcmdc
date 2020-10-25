import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ClientNode } from '@model/client.model';
import { Deployment } from '@model/deployment.model';
import { DeploymentsService } from '@services/deployments.service';
import { EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

const CONFIGURATION_VALIDATOR: ValidatorFn = (control: AbstractControl) => {

  try {
    JSON.parse(control.value);
  } catch (error) {
    return {
      json: 'Not json parsable'
    }
  }
  return null;
}

@Component({
  selector: 'app-deployment-edit',
  templateUrl: './deployment-edit.component.html',
  styleUrls: ['./deployment-edit.component.css']
})
export class DeploymentEditComponent implements OnInit {

  group: FormGroup = this.fb.group({
    name: this.fb.control(''),
    configuration: this.fb.control('', [Validators.required, CONFIGURATION_VALIDATOR]),
    id: this.fb.control(''),
    containerName: this.fb.control(''),
    targetNodes: this.fb.control(''),
  });

  deployment: Deployment;

  constructor(private service: DeploymentsService, private activatedRoute: ActivatedRoute, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(flatMap(params => {
      if (params['id'] != undefined) {
        return this.service.getById(params['id']);
      } else {
        return EMPTY;
      }
    })).subscribe(d => {

      const dCopy: Deployment = Object.create(null);
      Object.assign(dCopy, d);
      dCopy.configuration = JSON.stringify(JSON.parse(d.configuration), null, 2);
      this.group.patchValue(dCopy);
      console.log(this.group);
    });
  }

  private addTargetNode(node: ClientNode) {
  }

}
