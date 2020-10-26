import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ClientNode } from '@model/client.model';
import { ClientsService } from '@services/clients.service';
import { Observable } from 'rxjs';
import { filter, map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-select-client-node',
  templateUrl: './select-client-node.component.html',
  styleUrls: ['./select-client-node.component.css']
})
export class SelectClientNodeComponent implements OnInit {
  control = new FormControl();

  clientNodes: ClientNode[] = undefined;
  filteredNodes: Observable<ClientNode[]>;
  constructor(private clientService: ClientsService, private dialogRef: MatDialogRef<SelectClientNodeComponent>) {
  }

  ngOnInit(): void {
    this.clientService.getClients().subscribe(clients => this.clientNodes = clients);
    this.filteredNodes = this.control.valueChanges.pipe(startWith(''),
      map(value => typeof value === 'string' ? value : value.name),
      map(value => this._filterNodes(value))
    );
  }

  private _filterNodes(value: string): ClientNode[] {
    let filtered = this.clientNodes.filter(node => node.name.toLowerCase().includes(value.toLowerCase()));
    console.log(filtered)
    return filtered;
  }

  displayFn(node: ClientNode) {
    if (node != null && node != undefined) {
      return node.name;

    } else {
      return '';
    }
  }

  onCancelClick() {
    this.dialogRef.close();
  }

}
