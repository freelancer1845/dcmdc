import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PagesRoutingModule } from './pages-routing.module';
import { MainDashboardComponent } from './main-dashboard/main-dashboard.component';
import { ComponentsModule } from '@components/components.module';
import { ClientNodePageComponent } from './client-node-page/client-node-page.component';
import { ClientsPageComponent } from './clients-page/clients-page.component';

@NgModule({
  declarations: [MainDashboardComponent, ClientNodePageComponent, ClientsPageComponent],
  imports: [
    CommonModule,
    PagesRoutingModule,
    ComponentsModule,
  ]
})
export class PagesModule { }
