import { iEmployee } from "./employee";
import { iTravel } from "./travel";

export interface iReservation {
  id?: number;
  travel?: iTravel;
  employee?: iEmployee;
  requestDate: Date;
  description: string;
}
