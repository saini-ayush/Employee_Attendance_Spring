package com.consultadd.CURDApp.services;


import com.consultadd.CURDApp.Modals.Attendence;
import com.consultadd.CURDApp.Modals.Employee;
import com.consultadd.CURDApp.dto.AttendanceDTO;
import com.consultadd.CURDApp.repositories.AttendanceRepository;
import com.consultadd.CURDApp.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public AttendanceDTO checkIn(AttendanceDTO attendanceDTO) {
        Optional<Employee> employeeOpt = employeeRepository.findById(attendanceDTO.getEmployeeId());

        if (employeeOpt.isPresent()) {
            Attendence attendance = new Attendence();
            attendance.setEmployee(employeeOpt.get());
            attendance.setCheckInTime(LocalDateTime.now());
            attendance.setNote(attendanceDTO.getNotes());

            Attendence savedAttendance = attendanceRepository.save(attendance);
            return convertToDTO(savedAttendance);
        }

        return null;
    }

    public AttendanceDTO checkOut(Long attendanceId) {
        Optional<Attendence> attendanceOpt = attendanceRepository.findById(attendanceId);

        if (attendanceOpt.isPresent()) {
            Attendence attendance = attendanceOpt.get();
            attendance.setCheckOutTime(LocalDateTime.now());

            Attendence updatedAttendance = attendanceRepository.save(attendance);
            return convertToDTO(updatedAttendance);
        }

        return null;
    }

    public List<AttendanceDTO> getAttendanceByEmployeeId(Long employeeId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);

        if (employeeOpt.isPresent()) {
            List<Attendence> attendances = attendanceRepository.findByEmployee(employeeOpt.get());
            return attendances.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    public List<AttendanceDTO> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AttendanceDTO convertToDTO(Attendence attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setEmployeeId(attendance.getEmployee().getId());
        dto.setCheckInTime(attendance.getCheckInTime());
        dto.setCheckOutTime(attendance.getCheckOutTime());
        dto.setNotes(attendance.getNote());
        return dto;
    }
}
