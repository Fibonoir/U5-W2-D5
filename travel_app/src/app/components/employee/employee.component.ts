import { Component, OnInit } from '@angular/core';
import { iEmployee } from '../../interfaces/employee';
import { iProfilePicture } from '../../interfaces/profile-picture';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.css'
})
export class EmployeeComponent implements OnInit {
  employees: iEmployee[] = [];
  newEmployee: iEmployee = {
    username: "",
    name: "",
    lastName: "",
    email: "",
  }

  selectedEmployee: iEmployee | null = null;
  profilePicture: string | ArrayBuffer | null = null;
  uploadFile: File | null = null;
  selectedImageUrl: string | null = null;
  defaultProfilePictureUrl: string =  "https://static.vecteezy.com/system/resources/previews/020/765/399/non_2x/default-profile-account-unknown-icon-black-silhouette-free-vector.jpg"

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.fetchEmployees();
  }

  fetchEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => this.employees = data,
      error: (err) => console.error(err),
    });
  }

  createEmployee(): void {
    if (!this.newEmployee.username || !this.newEmployee.email) {
      alert('Username and Email are required.');
      return;
    }

    const usernameExists = this.employees.some(emp => emp.username === this.newEmployee.username);
    if (usernameExists) {
      alert('Username already exists.');
      return;
    }

    const emailExists = this.employees.some(emp => emp.email === this.newEmployee.email);
    if (emailExists) {
      alert('Email already exists.');
      return;
    }

    this.employeeService.createEmployee(this.newEmployee).subscribe({
      next: () => {
        this.newEmployee = { username: '', name: '', lastName: '', email: '' };
        this.fetchEmployees();
      },
      error: (err) => console.error(err),
    });
  }

  selectEmployee(employee: iEmployee): void {
    this.selectedEmployee = { ...employee };

  }

  updateEmployee(): void {
    if (!this.selectedEmployee) return;
    if (!this.selectedEmployee.username || !this.selectedEmployee.email) {
      alert('Username and Email are required.');
      return;
    }
    this.employeeService.updateEmployee(this.selectedEmployee.id!, this.selectedEmployee).subscribe({
      next: () => {
        this.selectedEmployee = null;
        this.fetchEmployees();
      },
      error: (err) => console.error(err),
    });
  }

  deleteEmployee(id: number): void {
    if (!confirm('Are you sure you want to delete this employee?')) return;
    this.employeeService.deleteEmployee(id).subscribe({
      next: () => {
        this.employees = this.employees.filter(employee => employee.id !== id);
      },
      error: (err) => console.error(err),
    });
  }

  onFileSelected(event: any, id: number): void {
    const file: File = event.target.files[0];
    if (file) {
      this.uploadFile = file;

      // Preview the image
      const reader = new FileReader();
      reader.onload = e => this.profilePicture = reader.result;
      reader.readAsDataURL(file);

      // Upload the profile picture
      this.employeeService.uploadProfilePicture(id, this.uploadFile).subscribe({
        next: () => {
          this.uploadFile = null;
          this.profilePicture = null;
          this.fetchEmployees();
        },
        error: (err) => console.error(err),
      });
    } else {
      alert('No file selected.');
    }
  }

  viewProfilePicture(id: number): void {
    const employee = this.employees.find(emp => emp.id === id);
    if (employee && employee.profilePictureUrl) {
      this.selectedImageUrl = employee.profilePictureUrl;
    }
  }

  closeModal(): void {
    this.selectedImageUrl = null;
  }

  cancelEdit(): void {
    this.selectedEmployee = null;
    this.profilePicture = null;
  }

  deleteProfilePicture(id: number): void {
    if (!confirm('Are you sure you want to delete this profile picture?')) return;
    this.employeeService.deleteProfilePicture(id).subscribe({
      next: () => {
        this.fetchEmployees();
      },
      error: (err) => console.error(err),
    });
  }
}
