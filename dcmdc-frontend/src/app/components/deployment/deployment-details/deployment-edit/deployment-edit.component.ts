import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { SelectClientNodeComponent } from '@components/dialgos/select-client-node/select-client-node.component';
import { ClientNode } from '@model/client.model';
import { Deployment } from '@model/deployment.model';
import { DeploymentsService } from '@services/deployments.service';
import { EMPTY } from 'rxjs';
import { filter, flatMap } from 'rxjs/operators';

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

  constructor(private service: DeploymentsService, private activatedRoute: ActivatedRoute, private fb: FormBuilder, private dialog: MatDialog) { }

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


  public addNode() {
    this.dialog.open(SelectClientNodeComponent).afterClosed().pipe(filter(v => v != undefined)).subscribe(value => {
      if (this.group.value['targetNodes'].find((node: ClientNode) => node.api_id == value.api_id) != null) {
        console.log("Node already a target node");
        return;
      } else {
        this.group.value['targetNodes'].push(value);
      }
    });
  }

  public removeNode(nodeToRemove: ClientNode) {
    this.group.value['targetNodes'] = this.group.value['targetNodes'].filter((node: ClientNode) => node.api_id != nodeToRemove.api_id);
  }
}
