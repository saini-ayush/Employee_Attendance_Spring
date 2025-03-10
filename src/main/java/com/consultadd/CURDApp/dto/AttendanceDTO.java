package com.consultadd.CURDApp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceDTO {
    private Long id;
    private Long employeeId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String notes;
}
