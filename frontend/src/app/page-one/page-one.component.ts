import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-page-one',
  templateUrl: './page-one.component.html',
  styleUrls: ['./page-one.component.css']
})
export class PageOneComponent {
  selectedFile: File | null = null;
  uploadMessage: string = '';

  constructor(private http: HttpClient) {}

  onFileSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      this.selectedFile = target.files[0];
    }
  }

  uploadDocument() {
    if (!this.selectedFile) return;

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('description', 'Uploaded via frontend UI');

    this.http.post('http://localhost:8081/document', formData, { responseType: 'text' }).subscribe(
      response => {
        this.uploadMessage = 'Datei erfolgreich hochgeladen!';
      },
      error => {
        console.error('Fehler beim Hochladen:', error);
        this.uploadMessage = 'Fehler beim Hochladen der Datei.';
      }
    );
  }
}
