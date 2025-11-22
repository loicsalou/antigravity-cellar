import { Routes } from '@angular/router';
import { BottleListComponent } from './pages/bottle-list/bottle-list.component';

export const routes: Routes = [
    { path: '', redirectTo: 'bottles', pathMatch: 'full' },
    { path: 'bottles', component: BottleListComponent }
];
