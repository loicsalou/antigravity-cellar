import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WineService } from '../../services/wine';

@Component({
    selector: 'app-add-wine',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './add-wine.component.html',
})
export class AddWineComponent implements OnInit {
    formData = {
        name: '',
        vintage: null as number | null,
        appellation: '',
        color: '',
        producerId: null as number | null,
        regionId: null as number | null
    };

    loading = false;
    error = '';

    wineColors = [
        { value: 'RED', label: 'Rouge' },
        { value: 'WHITE', label: 'Blanc' },
        { value: 'ROSE', label: 'Rosé' },
        { value: 'SPARKLING', label: 'Effervescent' },
        { value: 'YELLOW', label: 'Jaune' },
        { value: 'DESSERT', label: 'Liquoreux' }
    ];

    constructor(
        private wineService: WineService,
        private router: Router
    ) { }

    ngOnInit() {
        // Load producers and regions if needed
    }

    onSubmit() {
        if (!this.formData.name || !this.formData.color) {
            this.error = 'Veuillez remplir les champs obligatoires';
            return;
        }

        this.loading = true;
        this.error = '';

        const wineData: any = {
            name: this.formData.name,
            vintage: this.formData.vintage,
            appellation: this.formData.appellation,
            color: this.formData.color
        };

        // Add producer and region if provided
        if (this.formData.producerId) {
            wineData.producer = { id: this.formData.producerId };
        }
        if (this.formData.regionId) {
            wineData.region = { id: this.formData.regionId };
        }

        this.wineService.createWine(wineData).subscribe({
            next: () => {
                this.loading = false;
                this.router.navigate(['/bottles/add']);
            },
            error: (err) => {
                this.loading = false;
                this.error = err.error?.message || 'Une erreur est survenue lors de la création du vin';
                console.error('Error creating wine', err);
            }
        });
    }

    onCancel() {
        this.router.navigate(['/bottles/add']);
    }
}
