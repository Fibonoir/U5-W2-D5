import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { iTravel } from '../interfaces/travel';

@Injectable({
  providedIn: 'root'
})
export class TravelService {

  private apiUrl = "http://localhost:8080/api/travels";

  constructor(private http: HttpClient) { }

  getAllTravels(): Observable<iTravel[]> {
    return this.http.get<iTravel[]>(this.apiUrl);
  }

  getTravelById(id: number): Observable<iTravel> {
    return this.http.get<iTravel>(`${this.apiUrl}/${id}`);
  }

  createTravel(travel: iTravel): Observable<iTravel> {
    return this.http.post<iTravel>(this.apiUrl, travel);
  }

  updateTravel(id: number, travel: iTravel): Observable<iTravel> {
    return this.http.put<iTravel>(`${this.apiUrl}/${id}`, travel);
  }

  deleteTravel(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  changeStatus(id: number, status: string): Observable<iTravel> {
    return this.http.put<iTravel>(`${this.apiUrl}/${id}/status?status=${status}`, {});
  }
}
