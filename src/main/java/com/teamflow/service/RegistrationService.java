package com.teamflow.service;

import com.teamflow.entity.AppUser;
import com.teamflow.entity.Employee;
import com.teamflow.repository.AppUserRepository;
import com.teamflow.repository.EmployeeRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            AppUserRepository appUserRepository,
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(String username, String password, String role) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters");
        }

        if (!"EMPLOYEE".equals(role)) {
            throw new IllegalArgumentException("Invalid registration role");
        }

        username = username.trim();

        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("EMPLOYEE");

        appUserRepository.save(user);

        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setName(username);
        employee.setRole("EMPLOYEE");

        employeeRepository.save(employee);
    }
}