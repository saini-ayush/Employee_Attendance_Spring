package com.consultadd.CURDApp.services;


import com.consultadd.CURDApp.Modals.Attendence;
import com.consultadd.CURDApp.Modals.Employee;
import com.consultadd.CURDApp.dto.AttendanceDTO;
import com.consultadd.CURDApp.exception.BadRequestException;
import com.consultadd.CURDApp.exception.ResourceNotFoundException;
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
        if (attendanceDTO.getEmployeeId() == null) {
            throw new BadRequestException("Employee ID is required for check-in");
        }

        Employee employee = employeeRepository.findById(attendanceDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", attendanceDTO.getEmployeeId()));

        Attendence attendance = new Attendence();
        attendance.setEmployee(employee);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setNote(attendanceDTO.getNotes());

        Attendence savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public AttendanceDTO checkOut(Long attendanceId) {
        Attendence attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", attendanceId));

        if (attendance.getCheckOutTime() != null) {
            throw new BadRequestException("Employee has already checked out for this attendance record");
        }

        attendance.setCheckOutTime(LocalDateTime.now());

        Attendence updatedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(updatedAttendance);
    }

    public List<AttendanceDTO> getAttendanceByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        List<Attendence> attendances = attendanceRepository.findByEmployee(employee);
        return attendances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
