package com.consultadd.CURDApp.controller;

import com.consultadd.CURDApp.dto.AttendanceDTO;
import com.consultadd.CURDApp.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService){
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDTO> checkIn(@RequestBody AttendanceDTO attendanceDTO){
        AttendanceDTO attendance = attendanceService.checkIn(attendanceDTO);

        if (attendance != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/check-out/{attendanceId}")
    public ResponseEntity<AttendanceDTO> checkOut(@PathVariable Long attendanceId){
        AttendanceDTO attendance = attendanceService.checkOut(attendanceId);

        if(attendance != null){
            return ResponseEntity.ok(attendance);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByEmployeeId(@PathVariable Long employeeId){
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByEmployeeId(employeeId);

        return ResponseEntity.ok(attendance);
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDTO>> getAllAttendance(){
        List<AttendanceDTO> attendance  = attendanceService.getAllAttendance();

        return  ResponseEntity.ok(attendance);
    }
}
