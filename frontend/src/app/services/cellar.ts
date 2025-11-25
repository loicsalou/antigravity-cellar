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

  getUserCellars(): Observable<Cellar[]> {
    return this.http.get<Cellar[]>(this.apiUrl);
  }

  getCellar(id: number): Observable<Cellar> {
    return this.http.get<Cellar>(`${this.apiUrl}/${id}`);
  }

  createCellar(cellar: Partial<Cellar>): Observable<Cellar> {
    return this.http.post<Cellar>(this.apiUrl, cellar);
  }

  deleteCellar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
