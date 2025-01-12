package com.epicode.it.tavel_manager.repository;

import com.epicode.it.tavel_manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
