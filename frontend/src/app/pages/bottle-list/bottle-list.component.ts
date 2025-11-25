import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BottleService } from '../../services/bottle.service';
import { Bottle } from '../../models/bottle.model';

@Component({
    selector: 'app-bottle-list',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink],
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
    searchRegion = '';
    searchAppellation = '';

    // Pagination
    currentPage: number = 0;
    pageSize: number = 10;
    totalItems: number = 0;
    totalPages: number = 0;
    pageSizes: number[] = [5, 10, 15, 20, 25];

    constructor(
        private bottleService: BottleService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            this.searchRegion = params['region'] || '';
            this.searchAppellation = params['appellation'] || '';
            this.loadBottles();
        });
    }

    loadBottles(): void {
        this.loading = true;
        this.error = '';

        this.bottleService.getAllBottles({
            query: this.searchQuery,
            vintage: this.searchVintage || undefined,
            color: this.searchColor,
            region: this.searchRegion || undefined,
            appellation: this.searchAppellation || undefined,
            page: this.currentPage,
            size: this.pageSize
        }).subscribe({
            next: (response) => {
                this.bottles = response.content;
                this.totalItems = response.totalElements;
                this.totalPages = response.totalPages;
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
        this.currentPage = 0; // Reset to first page on new search
        // Clear region/appellation filters when searching manually
        this.searchRegion = '';
        this.searchAppellation = '';
        // Update URL to remove query params
        this.router.navigate([], {
            relativeTo: this.route,
            queryParams: { region: null, appellation: null },
            queryParamsHandling: 'merge'
        });
        this.loadBottles();
    }

    resetSearch(): void {
        this.searchQuery = '';
        this.searchVintage = null;
        this.searchColor = '';
        this.currentPage = 0;
        this.pageSize = 10;
        this.loadBottles();
    }

    onPageChange(page: number): void {
        this.currentPage = page;
        this.loadBottles();
    }

    onPageSizeChange(event: any): void {
        this.pageSize = +event.target.value;
        this.currentPage = 0;
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

    get visiblePages(): number[] {
        const pages: number[] = [];
        const total = this.totalPages;
        const current = this.currentPage;

        if (total <= 0) return [];

        // Always show 3 pages if possible: current-1, current, current+1
        let start = Math.max(0, current - 1);
        let end = Math.min(total - 1, current + 1);

        // Adjust if we are at the beginning
        if (current === 0) {
            end = Math.min(total - 1, start + 2);
        }

        // Adjust if we are at the end
        if (current === total - 1) {
            start = Math.max(0, end - 2);
        }

        for (let i = start; i <= end; i++) {
            pages.push(i);
        }
        return pages;
    }
}
