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

  ocrResults: any[] = [];

  constructor(private http: HttpClient) {}

  searchDocuments() {
    if (!this.searchTerm) {
      return;
    }

    this.http
      .get<any[]>(`http://localhost:8081/search?term=${encodeURIComponent(this.searchTerm)}`)
      .subscribe(
        (data) => {
          this.searchResults = data;
        },
        (error) => {
          console.error('Fehler bei der Suche:', error);
        }
      );
  }

  searchOcrDocuments() {
    if (!this.searchTerm) {
      return;
    }

    this.http
      .get<any[]>(`http://localhost:8081/search-ocr?term=${encodeURIComponent(this.searchTerm)}`)
      .subscribe(
        (data) => {
          this.ocrResults = data;
          console.log('OCR-Suchergebnisse:', data);
        },
        (error) => {
          console.error('Fehler bei der OCR-Suche:', error);
        }
      );
  }

  viewDocument(fileName: string) {
    this.http.get(`http://localhost:8081/document/${encodeURIComponent(fileName)}`, {
      responseType: 'blob'
    }).subscribe(blob => {
        console.log("blob size =", blob.size);
        console.log("blob type =", blob.type);

        const pdfBlob = new Blob([blob], { type: "application/pdf" });

        const blobUrl = URL.createObjectURL(pdfBlob);
        console.log("blobUrl =", blobUrl);

        this.pdfContent = blobUrl;
      },
      error => {
        console.error("Fehler beim Laden des PDFs:", error);
      });
  }
}
