import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import { Wine } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class WineService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/wines';

  getAllWines(): Observable<Wine[]> {
    return this.http.get<Wine[]>(this.apiUrl).pipe(
      tap(wines => console.log(wines)),
      catchError(error => {
        console.error('Error fetching wines:', error);
        return of([]);
      })
    );
  }

  getWine(id: number): Observable<Wine> {
    return this.http.get<Wine>(`${this.apiUrl}/${id}`);
  }

  createWine(wine: Partial<Wine>): Observable<Wine> {
    return this.http.post<Wine>(this.apiUrl, wine);
  }
}
