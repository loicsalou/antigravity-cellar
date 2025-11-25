import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bottle } from '../models/bottle.model';

@Injectable({
    providedIn: 'root'
})
export class BottleService {
    private apiUrl = '/api/bottles';

    constructor(private http: HttpClient) { }

    getAllBottles(params?: { query?: string; vintage?: number; color?: string; region?: string; appellation?: string; page?: number; size?: number }): Observable<any> {
        let queryParams = new HttpParams();
        if (params?.query) queryParams = queryParams.set('query', params.query);
        if (params?.vintage) queryParams = queryParams.set('vintage', params.vintage);
        if (params?.color) queryParams = queryParams.set('color', params.color);
        if (params?.region) queryParams = queryParams.set('region', params.region);
        if (params?.appellation) queryParams = queryParams.set('appellation', params.appellation);
        if (params?.page !== undefined) queryParams = queryParams.set('page', params.page);
        if (params?.size !== undefined) queryParams = queryParams.set('size', params.size);

        return this.http.get<any>(this.apiUrl, { params: queryParams });
    }

    addBottleBatch(request: any): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/batch`, request);
    }
}
