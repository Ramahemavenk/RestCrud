package com.hcl.uob.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.uob.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT * from employee WHERE email = :email", nativeQuery = true)
	Optional<Employee> findByEmail(@Param ("email") String email);
	

}
