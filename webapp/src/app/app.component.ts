import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';
import {DatePipe} from '@angular/common';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{


  searchResults: any;
  q: string;
  sortField: string;
  sortOrder: string;
  page: number;
  size: number = 10 ;
  searchForm: FormGroup;


  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.searchForm = fb.group({});
  }

  ngOnInit(): void {
    this.clear();
  }

  public hasResults(): boolean {
    return this.searchResults != null && this.searchResults.content != null && this.searchResults.content.length > 0;
  }

  public search(): void {
    this.resetState();
    this.executeSearch();
  }

  private executeSearch(): void {
    let params: string = '';
    params += 'page=' + this.page + '&size=' + this.size;
    if (this.sortField != null && this.sortField != '') {
      params += '&sort=' + this.sortField + "," + this.sortOrder
    }
    if (this.q != null && this.q != '') {
      params += '&q=' + this.q
    }
    this.http.get(environment.eventRestEndpoint + '?' + params).subscribe((res: any) => {
      this.searchResults = res;
    });
  }

  private resetState(): void {
    this.page = 1;
    this.sortField = 'startTime';
    this.sortOrder = 'asc';
  }

  public clear(): void {
    this.resetState();
    this.q = '';
    this.executeSearch();
  }

  public sort(column: string): void {
    if (column != this.sortField) {
       this.resetState();
       this.sortField = column;
       this.executeSearch();
    } else {
      this.page = 1;
      this.sortOrder= this.sortOrder == 'asc' ? 'desc' : 'asc';
      this.executeSearch();
    }
  }



}
