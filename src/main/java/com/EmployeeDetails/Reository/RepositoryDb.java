package com.EmployeeDetails.Reository;

import com.EmployeeDetails.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryDb extends JpaRepository<Employee, Integer> {

}
