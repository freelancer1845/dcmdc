import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DeploymentDetailsComponent } from '@components/deployment/deployment-details/deployment-details.component';
import { ClientNodePageComponent } from './client-node-page/client-node-page.component';
import { ClientsPageComponent } from './clients-page/clients-page.component';
import { MainDashboardComponent } from './main-dashboard/main-dashboard.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'deployments',
  },
  {
    path: 'deployments',
    component: MainDashboardComponent,
  },
  {
    path: 'client-node/:uuid',
    component: ClientNodePageComponent,
  },
  {
    path: 'clients',
    component: ClientsPageComponent,
  }, {
    path: 'deployments/details/:id',
    component: DeploymentDetailsComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
