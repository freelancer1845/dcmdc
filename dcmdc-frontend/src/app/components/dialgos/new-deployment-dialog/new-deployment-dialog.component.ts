import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { DeploymentsService } from '@services/deployments.service';

@Component({
  selector: 'app-new-deployment-dialog',
  templateUrl: './new-deployment-dialog.component.html',
  styleUrls: ['./new-deployment-dialog.component.css']
})
export class NewDeploymentDialogComponent implements OnInit {
  nameControl = new FormControl('', Validators.required);
  containerNameControl = new FormControl('');
  constructor(private ref: MatDialogRef<NewDeploymentDialogComponent>, private service: DeploymentsService) { }

  ngOnInit(): void {
  }

  onCancel() {
    this.ref.close();
  }

  onCreate() {
    this.service.create({
      name: this.nameControl.value,
      containerName: this.containerNameControl.value,
    }).subscribe();
    this.ref.close();
  }
}
