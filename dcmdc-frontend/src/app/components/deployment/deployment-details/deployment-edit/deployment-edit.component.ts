import { ThrowStmt } from '@angular/compiler';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { SelectClientNodeComponent } from '@components/dialgos/select-client-node/select-client-node.component';
import { ClientNode } from '@model/client.model';
import { Deployment } from '@model/deployment.model';
import { DeploymentsService } from '@services/deployments.service';
import { EMPTY, Subject } from 'rxjs';
import { filter, flatMap, takeUntil } from 'rxjs/operators';
import { CreatePortBindingDialogComponent } from './create-port-binding-dialog/create-port-binding-dialog.component';

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
interface PortBinding {
  targetPort: number;
  targetProtocol: string;
  hostIp: string;
  hostPort: string;
}

@Component({
  selector: 'app-deployment-edit',
  templateUrl: './deployment-edit.component.html',
  styleUrls: ['./deployment-edit.component.css']
})
export class DeploymentEditComponent implements OnInit, OnDestroy {

  $destroy = new Subject();

  group: FormGroup = this.fb.group({
    name: this.fb.control(''),
    configuration: this.fb.control('', [Validators.required, CONFIGURATION_VALIDATOR]),
    id: this.fb.control(''),
    containerName: this.fb.control(''),
    creationTimestamp: this.fb.control(''),
    targetNodes: this.fb.control(''),
  });

  imageControl = this.fb.control('', [Validators.required]);
  portBindings: FormGroup[] = [];

  deployment: Deployment;

  constructor(private service: DeploymentsService, private activatedRoute: ActivatedRoute, private fb: FormBuilder, private dialog: MatDialog, private router: Router) { }
  ngOnDestroy(): void {
    this.$destroy.next(true);
  }

  ngOnInit(): void {
    this.activatedRoute.params.pipe(flatMap(params => {
      if (params['id'] != undefined) {
        return this.service.getById(params['id']);
      } else {
        return EMPTY;
      }
    })).subscribe(d => this.setEditEntity(d));
    this.imageControl.valueChanges.pipe(takeUntil(this.$destroy)).subscribe(image => {
      try {
        const currentConfig = JSON.parse(this.group.value['configuration']);
        currentConfig['Image'] = image;
        this.patchConfiguration(currentConfig);
      } catch (error) {
        // ignore
      }
    })
  }
  onConfigurationChange() {
    try {
      const config = JSON.parse(this.group.value.configuration);
      this.imageControl.patchValue(config['Image']);
      this.updatePortBindings(config);

    } catch (error) {
      // ignore
    }
  }

  public updateDeploy() {
    this.service.patch(this.group.value).subscribe(saved => this.setEditEntity(saved));
  }

  private setEditEntity(d: Deployment) {
    const dCopy: Deployment = Object.create(null);
    Object.assign(dCopy, d);
    try {
      dCopy.configuration = JSON.stringify(JSON.parse(d.configuration), null, 2);
    } catch (error) {
      // ignore
    } this.group.patchValue(dCopy);
    this.onConfigurationChange();
  }

  public addNode() {
    this.dialog.open(SelectClientNodeComponent).afterClosed().pipe(filter(v => v != undefined)).subscribe(value => {
      if (this.group.value['targetNodes'].find((node: ClientNode) => node.apiId == value.apiId) != null) {
        console.log("Node already a target node");
        return;
      } else {
        this.group.value['targetNodes'].push(value);
      }
    });
  }

  public removeNode(nodeToRemove: ClientNode) {
    this.group.value['targetNodes'] = this.group.value['targetNodes'].filter((node: ClientNode) => node.apiId != nodeToRemove.apiId);
  }


  private patchConfiguration(newConfig: any) {
    this.group.get('configuration').patchValue(JSON.stringify(newConfig, null, 2));
  }

  private getCurrentConfig(): any {
    try {
      return JSON.parse(this.group.value['configuration']);
    } catch (error) {
      return null;
    }
  }


  private portBindingsListenerDestroy = new Subject();

  private updatePortBindings(config: any) {
    try {
      const hostConfig = config['HostConfig'];
      if (hostConfig) {
        const portBindings = hostConfig['PortBindings'];
        if (portBindings) {
          this.portBindingsListenerDestroy.next(true);

          this.portBindings = Object.keys(portBindings).map(binding => {
            const targetBinding = binding.split(/\//);
            const group = this.createPortBindingGroup(targetBinding[0], targetBinding[1], portBindings[binding][0]['HostIp'], portBindings[binding][0]['HostPort'])
            return group;
          });

        }
      }
    } catch (error) {
      // ignore
    }
  }

  private createPortBindingGroup(targetPort: string, targetProtocol: string, initialHostIp?: string, initialHostPort?: string) {
    const currentConfig = this.getCurrentConfig();
    if (currentConfig['HostConfig'] == undefined) {
      currentConfig['HostConfig'] = {}
    }
    if (currentConfig['HostConfig']['PortBindings'] == undefined) {
      currentConfig['HostConfig']['PortBindings'] = {};
      this.patchConfiguration(currentConfig);
    }

    const targetBinding = [targetPort, targetProtocol];
    const group = this.fb.group({
      targetPort: this.fb.control(targetBinding[0]),
      targetProtocol: this.fb.control(targetBinding[1], [Validators.required, control => {
        const value = control.value as string;
        if (value.match(/(^tcp$)|(^udp$)/) != null) {
          return null;
        } else {
          return { protocol: 'Must be either tcp or udp' };
        }
      }]),
      hostIp: this.fb.control(initialHostIp),
      hostPort: this.fb.control(initialHostPort, [Validators.required, Validators.min(1), Validators.max(65535)])
    });
    group.valueChanges.pipe(takeUntil(this.portBindingsListenerDestroy)).subscribe(bindingChange => {
      if (group.valid) {

        try {
          const config = this.getCurrentConfig();
          const bindings = config['HostConfig']['PortBindings'];
          const key = bindingChange['targetPort'] + '/' + bindingChange['targetProtocol'];
          bindings[key] = [{
            "HostIp": bindingChange['hostIp'],
            "HostPort": bindingChange['hostPort']
          }];
          this.patchConfiguration(config);

        } catch (error) {
          // ignore
        }
      }
    });
    return group;
  }

  public addPortBinding() {
    this.dialog.open(CreatePortBindingDialogComponent).afterClosed().pipe(filter(v => v != undefined)).subscribe(value => {
      if (this.portBindings.find(binding => binding.value['targetPort'] == value.targetPort && binding.value['targetProtocol'] == value.targetProtocol) != null) {
        // show error
      } else {
        this.portBindings.push(this.createPortBindingGroup(value.targetPort, value.targetProtocol, 'localhost', ''));
      }
    })
  }

  public deletePortBinding(binding: PortBinding) {
    try {
      const config = this.getCurrentConfig();
      const portBindings = config['HostConfig']['PortBindings'];
      delete portBindings[binding.targetPort + '/' + binding.targetProtocol];
      config['HostConfig']['PortBindings'] = portBindings;
      this.patchConfiguration(config);
      this.updatePortBindings(config);
    } catch (error) {
      //
      console.log(error);
    }
  }


  public isValid() {
    let portBindingsValid = true;

    for (let binding of this.portBindings) {
      if (binding.invalid == true) {
        portBindingsValid = false;
        break;
      }
    }

    return this.group.valid && this.imageControl.valid && portBindingsValid;
  }

  public delete() {
    this.service.delete(this.group.value.id).subscribe({
      complete: () => this.router.navigateByUrl('pages/deployments')
    })
  }
}
