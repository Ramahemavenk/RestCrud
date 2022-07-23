package com.hcl.uob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.uob.entity.Employee;
import com.hcl.uob.service.EmployeeService;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
 class EmployeeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private EmployeeService employeeService;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateEmployee() throws JsonProcessingException, Exception {

		Employee employee = new Employee(1, "Anji", "ram", "anji@gmail.com");

		given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));
		ResultActions response = mockMvc.perform(post("/api/employee").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		response.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));

	}

	@Test
	void testGetAllEmployees() throws Exception {
		List<Employee> listEmployee = new ArrayList<>();
		listEmployee.add(new Employee(1, "anji", "uppu", "anji@gmail.com"));
		listEmployee.add(new Employee(2, "rani", "uppu", "rani@gmail.com"));

		given(employeeService.getAllEmployees()).willReturn(listEmployee);
		ResultActions response = mockMvc.perform(get("/api/employee"));
		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listEmployee.size())));

	}

	@Test
	void testGetEmployeeById() throws Exception {

		long employeeId = 1L;

		Employee employee = Employee.builder().firstName("Ramesh").lastName("Uppu").email("ramesh@gmail.com").build();
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
		ResultActions response = mockMvc.perform(get("/api/employee/{id}", employeeId));
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));

	}

	@Test
	void testGetEmployeeByIdNeg() throws Exception {

		long employeeId = 1L;

		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(get("/api/employee/{id}", employeeId));

		response.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	void testUpdateEmployee() throws JsonProcessingException, Exception {
		long empolyeeId = 1L;

		Employee savedEmployee = Employee.builder().firstName("Ravi").lastName("Uppu").email("ravi@gmail.com").build();

		Employee updatedEmployee = Employee.builder().firstName("Ram").lastName("Jadav").email("ram@gmail.com").build();

		given(employeeService.getEmployeeById(empolyeeId)).willReturn(Optional.of(savedEmployee));

		given(employeeService.updateEmployee(any(Employee.class))).will((invocation) -> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(put("/api/employee/{id}", empolyeeId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

	}

	@Test
	void testUpdateEmployeeNeg() throws JsonProcessingException, Exception {
		long empolyeeId = 1L;

		Employee updatedEmployee = Employee.builder().firstName("Ram").lastName("Jadav").email("ram@gmail.com").build();

		given(employeeService.getEmployeeById(empolyeeId)).willReturn(Optional.empty());
		given(employeeService.updateEmployee(any(Employee.class)))
				.willAnswer((invocation) -> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(put("/api/employee/{id}", empolyeeId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedEmployee)));

		response.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	void testDeleteEmployee() throws Exception {

		long employeeId = 1L;

		willDoNothing().given(employeeService).deleteEmployee(employeeId);

		ResultActions respopnse = mockMvc.perform(delete("/api/employee/{id}", employeeId));

		respopnse.andExpect(status().isOk()).andDo(print());

	}

}
