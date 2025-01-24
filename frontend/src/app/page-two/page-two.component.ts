import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-page-two',
  templateUrl: './page-two.component.html',
  styleUrls: ['./page-two.component.css']
})
export class PageTwoComponent {
  searchTerm: string = '';

  // Bisherige Suche (Datenbank-Suche)
  searchResults: any[] = [];
  pdfContent: string | null = null;

  // Neu: für OCR-Ergebnisse
  ocrResults: any[] = [];

  constructor(private http: HttpClient) {}

  // Bestehende Methode für DB-Suche (Dateiname)
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

  // NEU: OCR-Suche
  searchOcrDocuments() {
    if (!this.searchTerm) {
      return;
    }

    this.http
      .get<any[]>(`http://localhost:8081/search-ocr?term=${encodeURIComponent(this.searchTerm)}`)
      .subscribe(
        (data) => {
          // Hier speichern wir die Treffer in ocrResults
          this.ocrResults = data;
          console.log('OCR-Suchergebnisse:', data);
        },
        (error) => {
          console.error('Fehler bei der OCR-Suche:', error);
        }
      );
  }

// Bestehende Methode für PDF-Anzeige
  viewDocument(fileName: string) {
    this.http.get(`http://localhost:8081/document/${encodeURIComponent(fileName)}`, {
      responseType: 'blob'
    }).subscribe(blob => {
        // 1) Prüfe Größe und Typ (nur fürs Debugging)
        console.log("blob size =", blob.size);
        console.log("blob type =", blob.type);

        // 2) Manuell als "application/pdf" deklarieren
        // Falls dein Server es korrekt liefert, kannst du
        // das eigentlich weglassen. Hier zur Sicherheit:
        const pdfBlob = new Blob([blob], { type: "application/pdf" });

        // 3) Blob-URL generieren
        const blobUrl = URL.createObjectURL(pdfBlob);
        console.log("blobUrl =", blobUrl);

        // 4) Zuweisen an Variable in der Component
        this.pdfContent = blobUrl;
      },
      error => {
        console.error("Fehler beim Laden des PDFs:", error);
      });
  }
}
