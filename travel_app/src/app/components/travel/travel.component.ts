import { ReservationService } from './../../services/reservation.service';
import { TravelService } from './../../services/travel.service';
import { Component, OnInit } from '@angular/core';
import { iTravel } from '../../interfaces/travel';

@Component({
  selector: 'app-travel',
  templateUrl: './travel.component.html',
  styleUrl: './travel.component.css'
})
export class TravelComponent implements OnInit{
  travels: iTravel[] = [];
  newTravel: iTravel = {
    destination: "",
    date: "",
    status: "SCHEDULED"
  };
  selectedTravelId: number | null = null;

  constructor(private travelService: TravelService, private reservationService: ReservationService) {}

  ngOnInit(): void {
      this.fetchTravels();
  }

  fetchTravels(): void {
    this.travelService.getAllTravels().subscribe({
      next: (data) => this.travels = data,
      error: (err) => console.error(err)
    });
  }

  createTravel(): void {
    this.travelService.createTravel(this.newTravel).subscribe({
      next: () => {
        this.newTravel = { destination: '', date: '', status: 'SCHEDULED' };
        this.fetchTravels();
      },
      error: (err) => console.error(err),
    });
  }

  deleteTravel(id: number): void {
    this.reservationService.getAllReservations().subscribe({
      next: (reservations) => {
        const linkedReservations = reservations.filter(reservation => reservation.travel && reservation.travel.id === id);
        if (linkedReservations.length > 0) {
            if (confirm('This travel has linked reservations. Do you really want to delete it?\n If you proceed all the linked reservations will be deleted as well')) {
            this.performDeleteTravel(id);
            }
        } else {
          this.performDeleteTravel(id);
        }
      },
      error: (err) => console.error(err),
    });
  }

  private performDeleteTravel(id: number): void {
    this.travelService.deleteTravel(id).subscribe({
      next: () => this.fetchTravels(),
      error: (err) => console.error(err),
    });
  }

  changeStatus(id: number, status: string): void {
    this.travelService.changeStatus(id, status).subscribe({
      next: () => this.fetchTravels(),
      error: (err) => console.error(err),
    });
  }


  editTravel(travel: iTravel): void {
    this.selectedTravelId = travel.id!;

  }

  isEditing(travelId: number): boolean {
    return this.selectedTravelId === travelId;
  }

  cancelEdit(): void {
    this.selectedTravelId = null;
    this.fetchTravels();
  }

  saveStatus(travel: iTravel): void {
    if (!travel.id || !travel.status) {
      alert('Status is required.');
      return;
    }

    this.travelService.changeStatus(travel.id, travel.status).subscribe({
      next: () => {
        this.selectedTravelId = null;
        this.fetchTravels();
        alert('Status updated successfully.');
      },
      error: (err) => {
        console.error(err);
        alert('Failed to update status.');
      },
    });
  }

}
