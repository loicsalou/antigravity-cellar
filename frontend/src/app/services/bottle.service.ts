import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bottle } from '../models/bottle.model';

@Injectable({
    providedIn: 'root'
})
export class BottleService {
    private apiUrl = '/api/bottles';

    constructor(private http: HttpClient) { }

    getAllBottles(params?: { query?: string; vintage?: number; color?: string }): Observable<Bottle[]> {
        let queryParams = '';
        if (params) {
            const parts = [];
            if (params.query) parts.push(`query=${encodeURIComponent(params.query)}`);
            if (params.vintage) parts.push(`vintage=${params.vintage}`);
            if (params.color) parts.push(`color=${params.color}`);
            if (parts.length > 0) queryParams = `?${parts.join('&')}`;
        }
        return this.http.get<Bottle[]>(`${this.apiUrl}${queryParams}`);
    }
}
