import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { iReservation } from '../interfaces/reservation';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient) { }

  createReservation(employeeId: number, travelId: number, reservation: iReservation): Observable<iReservation> {
    return this.http.post<iReservation>(`${this.apiUrl}/employee/${employeeId}/travel/${travelId}`, reservation);
  }

  getReservationById(id: number): Observable<iReservation> {
    return this.http.get<iReservation>(`${this.apiUrl}/${id}`);
  }

  getAllReservations(): Observable<iReservation[]> {
    return this.http.get<iReservation[]>(this.apiUrl);
  }

  updateReservation(id: number, reservation: iReservation): Observable<iReservation> {
    return this.http.put<iReservation>(`${this.apiUrl}/${id}`, reservation);
  }

  deleteReservation(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
