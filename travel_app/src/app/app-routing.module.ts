import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TravelComponent } from './components/travel/travel.component';
import { EmployeeComponent } from './components/employee/employee.component';
import { ReservationComponent } from './components/reservation/reservation.component';

const routes: Routes = [
  { path: '', redirectTo: 'travel', pathMatch: 'full' },
  { path: 'travel', component: TravelComponent },
  { path: 'employee', component: EmployeeComponent },
  { path: 'reservation', component: ReservationComponent },
  { path: '**', redirectTo: 'travel' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
