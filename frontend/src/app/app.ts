import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, HttpClientModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('frontend');
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

  logout() {
    this.http.post('/api/logout', {}).subscribe(() => {
      this.user = null;
      window.location.href = '/';
    });
  }
}
