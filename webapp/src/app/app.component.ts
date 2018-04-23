import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{


  searchResults: any;

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.http.get(environment.eventRestEndpoint).subscribe((res: any) => {
      this.searchResults = res;
    });
  }

  public hasResults(): boolean {
    return this.searchResults != null && this.searchResults.content != null && this.searchResults.content.length > 0;
  }


}
