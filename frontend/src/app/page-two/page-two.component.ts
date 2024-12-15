import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-page-two',
  templateUrl: './page-two.component.html',
  styleUrls: ['./page-two.component.css']
})
export class PageTwoComponent {
  searchTerm: string = '';
  searchResults: any[] = [];
  pdfContent: string | null = null;

  constructor(private http: HttpClient) {}

  searchDocuments() {
    if (!this.searchTerm) {
      return;
    }

    this.http.get<any[]>(`http://localhost:8081/search?term=${encodeURIComponent(this.searchTerm)}`).subscribe(
      (data) => {
        this.searchResults = data;
      },
      (error) => {
        console.error('Fehler bei der Suche:', error);
      }
    );
  }

  viewDocument(fileName: string) {
    this.http.get(`http://localhost:8081/document/${encodeURIComponent(fileName)}`, { responseType: 'blob' }).subscribe(
      (blob) => {
        const blobUrl = URL.createObjectURL(blob);
        this.pdfContent = blobUrl;
      },
      (error) => {
        console.error('Fehler beim Laden des PDFs:', error);
      }
    );
  }
}
