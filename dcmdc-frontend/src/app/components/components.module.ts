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
import { MatTabsModule } from '@angular/material/tabs';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ClientNodeDetailsComponent } from './client-node-details/client-node-details.component';
import { RouterModule } from '@angular/router';
import { DeploymentsListComponent } from './deployment/deployments-list/deployments-list.component';
import { DeploymentDetailsComponent } from './deployment/deployment-details/deployment-details.component';
import { DeploymentInfoComponent } from './deployment/deployment-details/deployment-info/deployment-info.component';
import { DeploymentEditComponent } from './deployment/deployment-details/deployment-edit/deployment-edit.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SelectClientNodeComponent } from './dialgos/select-client-node/select-client-node.component';
import { DeploymentExecutionsListComponent } from './deployment/deployment-details/deployment-info/deployment-executions-list/deployment-executions-list.component';
import { CreatePortBindingDialogComponent } from './deployment/deployment-details/deployment-edit/create-port-binding-dialog/create-port-binding-dialog.component';
import { NewClientDialogComponent } from './dialgos/new-client-dialog/new-client-dialog.component';
import { NewDeploymentDialogComponent } from './dialgos/new-deployment-dialog/new-deployment-dialog.component';

const MATERIAL_IMPORTS = [
  MatTableModule,
  MatToolbarModule,
  MatButtonModule,
  MatSidenavModule,
  MatIconModule,
  MatListModule,
  MatTabsModule,
  MatInputModule,
  MatFormFieldModule,
  MatDialogModule,
  MatAutocompleteModule,
  MatSelectModule,
]

@NgModule({
  declarations: [ClientNodesListComponent, NavigationComponent, ClientNodeDetailsComponent, DeploymentsListComponent, DeploymentDetailsComponent, DeploymentInfoComponent, DeploymentEditComponent, SelectClientNodeComponent, DeploymentExecutionsListComponent, CreatePortBindingDialogComponent, NewClientDialogComponent, NewDeploymentDialogComponent],
  imports: [
    CommonModule,
    RouterModule,
    ...MATERIAL_IMPORTS,
    FlexLayoutModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  exports: [
    ...MATERIAL_IMPORTS,
    FlexLayoutModule,
    ClientNodesListComponent,
    NavigationComponent,
    ClientNodeDetailsComponent,
    DeploymentsListComponent,
    DeploymentDetailsComponent,
    ReactiveFormsModule,
    FormsModule,
  ],
  entryComponents: [
    SelectClientNodeComponent,
    CreatePortBindingDialogComponent,
    NewClientDialogComponent
  ]
})
export class ComponentsModule { }
