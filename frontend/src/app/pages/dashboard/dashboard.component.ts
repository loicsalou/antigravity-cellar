import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

interface DashboardStats {
    totalBottles: number;
    bottlesByRegion: { [key: string]: number };
    bottlesByColor: { [key: string]: number };
    mostExpensiveBottles: any[];
    oldestBottles: any[];
}

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
    stats: DashboardStats | null = null;
    loading = true;
    error: string | null = null;

    constructor(private http: HttpClient) { }

    ngOnInit() {
        this.http.get<DashboardStats>('/api/dashboard').subscribe({
            next: (data) => {
                this.stats = data;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading dashboard stats', err);
                this.error = 'Impossible de charger les statistiques.';
                this.loading = false;
            }
        });
    }

    getBadgeColor(color: string): string {
        switch (color) {
            case 'RED': return 'bg-red-100 text-red-800 border-red-200';
            case 'WHITE': return 'bg-yellow-50 text-yellow-800 border-yellow-200';
            case 'ROSE': return 'bg-pink-100 text-pink-800 border-pink-200';
            case 'SPARKLING': return 'bg-emerald-100 text-emerald-800 border-emerald-200';
            case 'YELLOW': return 'bg-amber-100 text-amber-800 border-amber-200';
            case 'DESSERT': return 'bg-orange-100 text-orange-800 border-orange-200';
            default: return 'bg-gray-100 text-gray-800 border-gray-200';
        }
    }

    formatColor(color: string): string {
        const map: { [key: string]: string } = {
            'RED': 'Rouge', 'WHITE': 'Blanc', 'ROSE': 'Rosé',
            'SPARKLING': 'Effervescent', 'YELLOW': 'Jaune', 'DESSERT': 'Liquoreux'
        };
        return map[color] || color;
    }
}
