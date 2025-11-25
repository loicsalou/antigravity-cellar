import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BottleService } from '../../services/bottle.service';
import { WineService } from '../../services/wine';
import { CellarService } from '../../services/cellar';

@Component({
    selector: 'app-add-bottle-modal',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './add-bottle-modal.component.html',
})
export class AddBottleModalComponent implements OnInit {
    @Output() close = new EventEmitter<void>();
    @Output() bottleAdded = new EventEmitter<void>();

    wines: any[] = [];
    cellars: any[] = [];

    formData = {
        wineId: null as number | null,
        cellarId: null as number | null,
        rackId: null as number | null,
        price: null as number | null,
        purchaseDate: '',
        volume: 750,
        quantity: 1
    };

    loading = false;
    error = '';

    constructor(
        private bottleService: BottleService,
        private wineService: WineService,
        private cellarService: CellarService
    ) { }

    ngOnInit() {
        this.loadWines();
        this.loadCellars();
    }

    loadWines() {
        this.wineService.getAllWines().subscribe({
            next: (wines) => this.wines = wines,
            error: (err) => console.error('Error loading wines', err)
        });
    }

    loadCellars() {
        this.cellarService.getUserCellars().subscribe({
            next: (cellars) => this.cellars = cellars,
            error: (err) => console.error('Error loading cellars', err)
        });
    }

    onSubmit() {
        if (!this.formData.wineId || !this.formData.cellarId) {
            this.error = 'Veuillez sélectionner un vin et une cave';
            return;
        }

        this.loading = true;
        this.error = '';

        this.bottleService.addBottleBatch(this.formData).subscribe({
            next: () => {
                this.loading = false;
                this.bottleAdded.emit();
                this.onClose();
            },
            error: (err) => {
                this.loading = false;
                this.error = err.error || 'Une erreur est survenue lors de l\'ajout des bouteilles';
                console.error('Error adding bottles', err);
            }
        });
    }

    onClose() {
        this.close.emit();
    }
}
