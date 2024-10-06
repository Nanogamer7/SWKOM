import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-page-one',
  templateUrl: './page-one.component.html',
  styleUrls: ['./page-one.component.css'],
})
export class PageOneComponent {
  message: string = '';

  constructor(private http: HttpClient) {}

  getHelloWorld() {
    this.http
      .get('http://localhost:8081/helloworld', { responseType: 'text' })
      .subscribe(
        (response) => {
          this.message = response;
        },
        (error) => {
          console.error(error);
        }
      );
  }
}