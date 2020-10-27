import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-create-port-binding-dialog',
  templateUrl: './create-port-binding-dialog.component.html',
  styleUrls: ['./create-port-binding-dialog.component.css']
})
export class CreatePortBindingDialogComponent implements OnInit {

  group: FormGroup;

  protocols = ['tcp', 'udp'];

  constructor(private fb: FormBuilder, private ref: MatDialogRef<CreatePortBindingDialogComponent>) {

    this.group = this.fb.group({
      targetPort: fb.control('', [Validators.required, Validators.max(65535), Validators.min(0)]),
      targetProtocol: fb.control('tcp')
    });
  }

  ngOnInit(): void {
  }

  onCancelClick() {
    this.ref.close();
  }
}
