package com.example.multi_tenancy_couchbase.controller;

import com.example.multi_tenancy_couchbase.model.Employee;
import com.example.multi_tenancy_couchbase.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

  @Autowired
  private EmployeeRepository employeeRepository;

  @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee){
      Employee employee1=this.employeeRepository.save(employee);
      return new ResponseEntity<>(employee1, HttpStatus.OK);
    }

}
