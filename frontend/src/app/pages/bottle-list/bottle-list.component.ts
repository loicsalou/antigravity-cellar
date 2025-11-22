import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BottleService } from '../../services/bottle.service';
import { Bottle } from '../../models/bottle.model';

@Component({
    selector: 'app-bottle-list',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './bottle-list.component.html',
})
export class BottleListComponent implements OnInit {
    bottles: Bottle[] = [];
    loading = true;
    error = '';

    // Search filters
    searchQuery = '';
    searchVintage: number | null = null;
    searchColor = '';

    constructor(private bottleService: BottleService) { }

    ngOnInit(): void {
        this.loadBottles();
    }

    loadBottles(): void {
        this.loading = true;
        const params: any = {};
        if (this.searchQuery) params.query = this.searchQuery;
        if (this.searchVintage) params.vintage = this.searchVintage;
        if (this.searchColor) params.color = this.searchColor;

        this.bottleService.getAllBottles(params).subscribe({
            next: (data) => {
                this.bottles = data;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error fetching bottles', err);
                this.error = 'Impossible de charger les bouteilles. Vérifiez que le backend est lancé.';
                this.loading = false;
            }
        });
    }

    onSearch(): void {
        this.loadBottles();
    }

    resetSearch(): void {
        this.searchQuery = '';
        this.searchVintage = null;
        this.searchColor = '';
        this.loadBottles();
    }

    getBadgeColor(color: string): string {
        switch (color) {
            case 'RED': return 'bg-red-100 text-red-800 border-red-200';
            case 'WHITE': return 'bg-yellow-50 text-yellow-800 border-yellow-200';
            case 'ROSE': return 'bg-pink-100 text-pink-800 border-pink-200';
            case 'SPARKLING': return 'bg-slate-100 text-slate-800 border-slate-200';
            case 'YELLOW': return 'bg-amber-100 text-amber-800 border-amber-200';
            case 'DESSERT': return 'bg-orange-100 text-orange-800 border-orange-200';
            default: return 'bg-gray-100 text-gray-800 border-gray-200';
        }
    }

    formatVolume(ml: number): string {
        if (ml >= 1000) {
            return (ml / 1000) + ' L';
        }
        return ml + ' ml';
    }
}
