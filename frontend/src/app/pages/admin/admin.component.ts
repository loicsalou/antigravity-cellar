import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-admin',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './admin.component.html',
})
export class AdminComponent {
    selectedFile: File | null = null;
    cellarName: string = '';
    uploadStatus: 'idle' | 'uploading' | 'success' | 'error' = 'idle';
    errorMessage: string = '';

    constructor(private http: HttpClient) { }

    onFileSelected(event: any) {
        const file: File = event.target.files[0];
        if (file) {
            this.selectedFile = file;
            this.uploadStatus = 'idle';
            this.errorMessage = '';
        }
    }

    upload() {
        if (!this.selectedFile || !this.cellarName) {
            return;
        }

        this.uploadStatus = 'uploading';
        const formData = new FormData();
        formData.append('file', this.selectedFile);
        formData.append('cellarName', this.cellarName);

        this.http.post('/api/import/csv', formData, { responseType: 'text' }).subscribe({
            next: (response) => {
                this.uploadStatus = 'success';
                this.selectedFile = null;
                // Reset file input if possible or just leave it
            },
            error: (error) => {
                this.uploadStatus = 'error';
                this.errorMessage = error.error || 'Une erreur est survenue lors de l\'import.';
                console.error('Upload error:', error);
            }
        });
    }
}
