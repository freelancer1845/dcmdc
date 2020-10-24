import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ClientNodePageComponent } from './client-node-page/client-node-page.component';
import { MainDashboardComponent } from './main-dashboard/main-dashboard.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard',
  },
  {
    path: 'dashboard',
    component: MainDashboardComponent,
  },
  {
    path: 'client-node/:uuid',
    component: ClientNodePageComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
