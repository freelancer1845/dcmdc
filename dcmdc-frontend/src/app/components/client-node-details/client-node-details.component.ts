import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-client-node-details',
  templateUrl: './client-node-details.component.html',
  styleUrls: ['./client-node-details.component.css']
})
export class ClientNodeDetailsComponent implements OnInit {
  public uuid: string;

  constructor(private activatedRoute: ActivatedRoute) {
    this.activatedRoute.params.subscribe(params => {
      this.uuid = params['uuid'];
    })
  }

  ngOnInit(): void {
  }

}
