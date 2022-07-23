package com.hcl.uob.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.uob.dao.EmployeeRepository;
import com.hcl.uob.entity.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee saveEmployee(Employee employee) {

		Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
		if (savedEmployee.isPresent()) {
			throw new ResourceNotFoundException("Employee already exist with given email:" + employee.getEmail());

		}

		return employeeRepository.save(employee);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Optional<Employee> getEmployeeById(long id) {
		return employeeRepository.findById(id);
	}

	@Override
	public Employee updateEmployee(Employee updatedEmployee) {
		return employeeRepository.save(updatedEmployee);
	}

	@Override
	public void deleteEmployee(long id) {
		employeeRepository.deleteById(id);

	}

}
