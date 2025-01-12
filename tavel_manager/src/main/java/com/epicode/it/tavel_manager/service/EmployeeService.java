package com.epicode.it.tavel_manager.service;

import com.epicode.it.tavel_manager.exception.ResourceNotFoundException;
import com.epicode.it.tavel_manager.model.Employee;
import com.epicode.it.tavel_manager.model.ProfilePicture;
import com.epicode.it.tavel_manager.repository.EmployeeRepository;
import com.epicode.it.tavel_manager.repository.ProfilePictureRepository;
import com.epicode.it.tavel_manager.validation.ValidationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProfilePictureRepository profilePictureRepository;
    private final ValidationService validationService;

    public EmployeeService(EmployeeRepository employeeRepository, ProfilePictureRepository profilePictureRepository, ValidationService validationService) {
        this.employeeRepository = employeeRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.validationService = validationService;
    }


    public Employee createEmployee(Employee employee) {
        if (employee.getId() != null && employeeRepository.existsById(employee.getId())) {
            throw new IllegalArgumentException("Employee with id " + employee.getId() + " already exists");
        }
        validationService.validate(employee);
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesPaginated(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }


    public Employee updateEmployee(Long id,Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(id);

        existingEmployee.setUsername(updatedEmployee.getUsername());
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());

        validationService.validate(existingEmployee);
        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        profilePictureRepository.findByEmployeeId(id).ifPresent(profilePictureRepository::delete);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
    }

    public void uploadProfilePicture(Long employeeId, MultipartFile file) {
        Employee employee = getEmployeeById(employeeId);

        try {
            ProfilePicture profilePicture = profilePictureRepository.findByEmployeeId(employeeId)
                    .orElse(new ProfilePicture());

            profilePicture.setEmployee(employee);
            profilePicture.setContentType(file.getContentType());
            profilePicture.setData(file.getBytes());

            profilePictureRepository.save(profilePicture);
        } catch (IOException e) {
            throw new RuntimeException("Could not upload profile picture", e);
        }


    }

    @Transactional(readOnly = true)
    public ProfilePicture getProfilePicture(Long employeeId) {
        return profilePictureRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("No profile picture found for employee " + employeeId));
    }

    public void deleteProfilePicture(Long employeeId) {
        profilePictureRepository.findByEmployeeId(employeeId).ifPresent(profilePictureRepository::delete);
    }


}
