import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface DashboardStats {
    totalBottles: number;
    bottlesByRegion: { [key: string]: number };
    regionAppellationStats: { [region: string]: { [appellation: string]: number } };
    bottlesByColor: { [key: string]: number };
    mostExpensiveBottles: any[];
    oldestBottles: any[];
}

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
    stats: DashboardStats | null = null;
    loading = true;
    error: string | null = null;

    topRegions: { name: string, count: number }[] = [];
    otherRegions: { name: string, count: number }[] = [];
    othersCount = 0;

    // Popup state
    showPopup = false;
    popupTitle = '';
    popupItems: { name: string, count: number }[] = [];

    constructor(private http: HttpClient) { }

    ngOnInit() {
        this.http.get<DashboardStats>('/api/dashboard').subscribe({
            next: (data) => {
                this.stats = data;
                this.processRegions();
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading dashboard stats', err);
                this.error = 'Impossible de charger les statistiques.';
                this.loading = false;
            }
        });
    }

    processRegions() {
        if (!this.stats) return;

        const regions = Object.entries(this.stats.bottlesByRegion)
            .map(([name, count]) => ({ name, count }))
            .sort((a, b) => b.count - a.count);

        if (regions.length > 10) {
            this.topRegions = regions.slice(0, 10);
            this.otherRegions = regions.slice(10);
            this.othersCount = this.otherRegions.reduce((sum, r) => sum + r.count, 0);
        } else {
            this.topRegions = regions;
            this.otherRegions = [];
            this.othersCount = 0;
        }
    }

    openRegionPopup(regionName: string) {
        if (!this.stats || !this.stats.regionAppellationStats[regionName]) return;

        this.popupTitle = `Appellations - ${regionName}`;
        this.popupItems = Object.entries(this.stats.regionAppellationStats[regionName])
            .map(([name, count]) => ({ name, count }))
            .sort((a, b) => b.count - a.count);
        this.showPopup = true;
    }

    openOthersPopup() {
        if (!this.stats) return;

        this.popupTitle = 'Appellations - Autres Régions';
        const aggregatedAppellations: { [key: string]: number } = {};

        this.otherRegions.forEach(region => {
            const appStats = this.stats!.regionAppellationStats[region.name];
            if (appStats) {
                Object.entries(appStats).forEach(([app, count]) => {
                    aggregatedAppellations[app] = (aggregatedAppellations[app] || 0) + count;
                });
            }
        });

        this.popupItems = Object.entries(aggregatedAppellations)
            .map(([name, count]) => ({ name, count }))
            .sort((a, b) => b.count - a.count);
        this.showPopup = true;
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
