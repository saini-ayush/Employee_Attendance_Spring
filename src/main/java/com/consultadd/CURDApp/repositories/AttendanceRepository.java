package com.consultadd.CURDApp.repositories;

import com.consultadd.CURDApp.Modals.Attendence;
import com.consultadd.CURDApp.Modals.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendence, Long> {
    List<Attendence> findByEmployee(Employee employee);
}
