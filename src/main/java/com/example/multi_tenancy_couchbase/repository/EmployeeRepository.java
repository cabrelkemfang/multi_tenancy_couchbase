package com.example.multi_tenancy_couchbase.repository;

import com.example.multi_tenancy_couchbase.model.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee,String> {
}
