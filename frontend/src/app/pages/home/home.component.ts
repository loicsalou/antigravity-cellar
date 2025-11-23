import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
    user: any = null;

    constructor(private http: HttpClient) { }

    ngOnInit() {
        this.http.get('/api/user').subscribe({
            next: (user: any) => {
                this.user = user && user.name ? user : null;
            },
            error: () => {
                this.user = null;
            }
        });
    }

    loginWithGoogle() {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    }

    loginWithFacebook() {
        window.location.href = 'http://localhost:8080/oauth2/authorization/facebook';
    }
}
