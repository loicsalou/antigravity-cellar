import { Routes } from '@angular/router';
import { BottleListComponent } from './pages/bottle-list/bottle-list.component';
import { HomeComponent } from './pages/home/home.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'bottles', component: BottleListComponent },
    { path: 'bottles/add', loadComponent: () => import('./pages/add-bottle/add-bottle.component').then(m => m.AddBottleComponent) },
    { path: 'wines/add', loadComponent: () => import('./pages/add-wine/add-wine.component').then(m => m.AddWineComponent) },
    { path: 'admin', loadComponent: () => import('./pages/admin/admin.component').then(m => m.AdminComponent) }
];
