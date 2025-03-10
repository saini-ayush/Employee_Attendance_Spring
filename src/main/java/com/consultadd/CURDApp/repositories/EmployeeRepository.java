package com.consultadd.CURDApp.repositories;

import com.consultadd.CURDApp.Modals.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long> {

}
