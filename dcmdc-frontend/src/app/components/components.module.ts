import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClientNodesListComponent } from './client-nodes-list/client-nodes-list.component';
import { MatTableModule } from '@angular/material/table';
import { NavigationComponent } from './navigation/navigation.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { ClientNodeDetailsComponent } from './client-node-details/client-node-details.component';

const MATERIAL_IMPORTS = [
  MatTableModule,
  MatToolbarModule,
  MatButtonModule,
  MatSidenavModule,
  MatIconModule,
  MatListModule,
]

@NgModule({
  declarations: [ClientNodesListComponent, NavigationComponent, ClientNodeDetailsComponent],
  imports: [
    CommonModule,
    ...MATERIAL_IMPORTS,
  ],
  exports: [
    ...MATERIAL_IMPORTS,
    ClientNodesListComponent,
    NavigationComponent,
    ClientNodeDetailsComponent,
  ]
})
export class ComponentsModule { }
