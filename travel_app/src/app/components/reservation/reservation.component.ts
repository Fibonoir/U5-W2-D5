import { iEmployee } from './../../interfaces/employee';
import { Component } from '@angular/core';
import { iReservation } from '../../interfaces/reservation';
import { iTravel } from '../../interfaces/travel';
import { ReservationService } from '../../services/reservation.service';
import { EmployeeService } from '../../services/employee.service';
import { TravelService } from '../../services/travel.service';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css'
})
export class ReservationComponent {
  reservations: iReservation[] = [];
  newReservation: iReservation = {
    employee: undefined,
    travel: undefined,
    requestDate: new Date(),
    description: ''
  };
  selectedReservation: iReservation | null = null;
  employees: iEmployee[] = [];
  travels: iTravel[] = [];

  constructor(
    private reservationService: ReservationService,
    private employeeService: EmployeeService,
    private travelService: TravelService
  ) {}

  ngOnInit(): void {
    this.fetchReservations();
    this.fetchEmployees();
    this.fetchTravels();
  }

  fetchReservations(): void {
    this.reservationService.getAllReservations().subscribe({
      next: (data) => this.reservations = data,
      error: (err) => console.error(err),
    });
  }

  fetchEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => this.employees = data,
      error: (err) => console.error(err),
    });
  }

  fetchTravels(): void {
    this.travelService.getAllTravels().subscribe({
      next: (data) => this.travels = data,
      error: (err) => console.error(err),
    });
  }

  createReservation(): void {
    if (!this.newReservation.employee || !this.newReservation.travel || !this.newReservation.requestDate) {
      alert('Employee, Travel, and Request Date are required.');
      return;
    }
    if (new Date(this.newReservation.requestDate) < new Date(this.newReservation.travel.date)) {
      alert('Reservation date cannot be earlier than the travel start date.');
      return;
    }
    this.reservationService.createReservation(
      this.newReservation.employee.id!,
      this.newReservation.travel.id!,
      this.newReservation
    ).subscribe({
      next: () => {
        this.newReservation = { employee: undefined, travel: undefined, requestDate: new Date(), description: '' };
        this.fetchReservations();
      },
      error: (err) => {
        console.error(err);
        if (err.status === 409) {
          alert('Conflict: The employee already has a reservation on this date.');
        } else {
          alert('An error occurred while creating the reservation.');
        }
      },
    });
  }

  selectReservation(reservation: iReservation): void {
    if (reservation.id !== undefined) {
      this.reservationService.getReservationById(reservation.id).subscribe({
        next: (data) => this.selectedReservation = data,
        error: (err) => console.error(err),
      });
    } else {
      console.error('Reservation ID is undefined.');
    }
  }

  updateReservation(): void {
    if (!this.selectedReservation || !this.selectedReservation.employee || !this.selectedReservation.travel || !this.selectedReservation.requestDate) {
      alert('Employee, Travel, and Request Date are required.');
      return;
    }
    if (new Date(this.selectedReservation.requestDate) < new Date(this.selectedReservation.travel.date)) {
      alert('Reservation date cannot be earlier than the travel start date.');
      return;
    }
    this.reservationService.updateReservation(this.selectedReservation.id!, this.selectedReservation).subscribe({
      next: () => {
        this.selectedReservation = null;
        this.fetchReservations();
      },
      error: (err) => {
        console.error(err);
        if (err.status === 409) {
          alert('Conflict: The employee already has a reservation on this date.');
        } else {
          alert('An error occurred while updating the reservation.');
        }
      },
    });
  }

  deleteReservation(id: number): void {
    this.reservationService.deleteReservation(id).subscribe({
      next: () => this.fetchReservations(),
      error: (err) => console.error(err),
    });
  }

  cancelEdit(): void {
    this.selectedReservation = null;
  }
}
