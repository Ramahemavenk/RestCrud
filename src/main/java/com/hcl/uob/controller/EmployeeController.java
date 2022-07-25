package com.hcl.uob.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hcl.uob.entity.Employee;
import com.hcl.uob.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	@Autowired(required = true)
	private EmployeeService employeeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Employee createEmployee(@RequestBody Employee employee) {
	System.out.println("Hi my new commit");
		
		return employeeService.saveEmployee(employee);
	}

	@GetMapping
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId) {
		return employeeService.getEmployeeById(employeeId).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId,
			@RequestBody Employee employee) {
		return employeeService.getEmployeeById(employeeId).map(savedEmployee -> {
			savedEmployee.setFirstName(employee.getFirstName());
			savedEmployee.setLastName(employee.getLastName());
			savedEmployee.setEmail(employee.getEmail());

			Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
			return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
		}).orElseGet(() -> ResponseEntity.notFound().build());

	}

	@DeleteMapping("{id}")
	public ResponseEntity deleteEmployee(@PathVariable("id") long employeeId) {
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity("Employee ", HttpStatus.OK);

	}

}
