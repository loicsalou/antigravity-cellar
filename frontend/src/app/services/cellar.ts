import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cellar } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class CellarService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/cellars';

  // TODO: Remove userId hardcoding once Auth is implemented
  private defaultUserId = 1;

  getUserCellars(): Observable<Cellar[]> {
    return this.http.get<Cellar[]>(`${this.apiUrl}?userId=${this.defaultUserId}`);
  }

  getCellar(id: number): Observable<Cellar> {
    return this.http.get<Cellar>(`${this.apiUrl}/${id}`);
  }

  createCellar(cellar: Partial<Cellar>): Observable<Cellar> {
    return this.http.post<Cellar>(`${this.apiUrl}?userId=${this.defaultUserId}`, cellar);
  }

  deleteCellar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
