import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WineService } from '../../services/wine';
import { Wine } from '../../models/models';

@Component({
  selector: 'app-wine-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wine-list.html',
  styleUrl: './wine-list.css'
})
export class WineList implements OnInit {
  private wineService = inject(WineService);
  wines: Wine[] = [];

  ngOnInit() {
    this.loadWines();
  }

  loadWines() {
    this.wineService.getAllWines().subscribe({
      next: (data) => this.wines = data,
      error: (err) => console.error('Error loading wines', err)
    });
  }
}
