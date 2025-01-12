import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, forkJoin, map, Observable, of, switchMap } from 'rxjs';
import { iEmployee, } from '../interfaces/employee';
import { iProfilePicture } from '../interfaces/profile-picture';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private apiUrl = 'http://localhost:8080/api/employees';

  constructor(private http: HttpClient) { }

  getAllEmployees(): Observable<iEmployee[]> {
    return this.http.get<iEmployee[]>(this.apiUrl).pipe(
      switchMap(employees => {
        const requests = employees.map(emp =>
          this.getProfilePicture(emp.id!).pipe(
            map(blob => {
              if (blob.size > 0) {
                emp.profilePictureUrl = URL.createObjectURL(blob);
              } else {
                emp.profilePictureUrl = 'https://static.vecteezy.com/system/resources/previews/020/765/399/non_2x/default-profile-account-unknown-icon-black-silhouette-free-vector.jpg'; // fallback image
              }
              return emp;
            }),
            // Handle employees without a picture
            catchError(() => {
              emp.profilePictureUrl = 'https://static.vecteezy.com/system/resources/previews/020/765/399/non_2x/default-profile-account-unknown-icon-black-silhouette-free-vector.jpg';
              return of(emp);
            })
          )
        );
        return forkJoin(requests);
      })
    );
  }



  getEmployeeById(id: number): Observable<iEmployee> {
    return this.http.get<iEmployee>(`${this.apiUrl}/${id}`);
  }

  createEmployee(emp: iEmployee): Observable<iEmployee> {
    return this.http.post<iEmployee>(this.apiUrl, emp);
  }

  updateEmployee(id: number, emp: iEmployee): Observable<iEmployee> {
    return this.http.put<iEmployee>(`${this.apiUrl}/${id}`, emp);
  }

  deleteEmployee(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  uploadProfilePicture(id: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(`${this.apiUrl}/${id}/profile-picture`, formData);
  }

  getProfilePicture(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/profile-picture`, { responseType: 'blob' });
  }

  getProfilePictureUrl(id: number): string {
    return `${this.apiUrl}/${id}/profile-picture`;
  }


  deleteProfilePicture(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}/profile-picture`);
  }
}
