import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ClientsService } from '@services/clients.service';

@Component({
  selector: 'app-new-client-dialog',
  templateUrl: './new-client-dialog.component.html',
  styleUrls: ['./new-client-dialog.component.css']
})
export class NewClientDialogComponent implements OnInit {
  link = "stupid";
  constructor(private ref: MatDialogRef<NewClientDialogComponent>, private service: ClientsService) { }
  apiId;
  apiSecret;
  nameControl = new FormControl('', [Validators.required]);
  ngOnInit(): void {
    this.apiId = this.randomId();
    this.apiSecret = this.randomId();

    this.link = `docker run -d -e API_ID=${this.apiId} -e API_SECRET=${this.apiSecret} -v /var/run/docker.sock:/docker.sock riedeldev/dcmdc-client:latest`;

  }
  onCancel() {
    this.ref.close();
  }
  onCreate() {
    this.service.createClient(this.apiId, this.apiSecret, this.nameControl.value).subscribe();
    this.ref.close();
  }


  private randomId() {
    var charset = "ABCDEFGGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvw01234567889".match(/./g);
    var id = "";
    for (let i = 0; i < 64; i++) {
      id += charset[Math.floor(Math.random() * charset.length)];
    }
    return id;
  }
}
