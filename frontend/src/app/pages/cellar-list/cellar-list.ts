import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CellarService } from '../../services/cellar';
import { Cellar } from '../../models/models';

@Component({
  selector: 'app-cellar-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cellar-list.html',
  styleUrl: './cellar-list.css'
})
export class CellarList implements OnInit {
  private cellarService = inject(CellarService);
  cellars: Cellar[] = [];

  ngOnInit() {
    this.loadCellars();
  }

  loadCellars() {
    this.cellarService.getUserCellars().subscribe({
      next: (data) => this.cellars = data,
      error: (err) => console.error('Error loading cellars', err)
    });
  }
}
