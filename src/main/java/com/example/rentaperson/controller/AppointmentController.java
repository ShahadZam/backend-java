package com.example.rentaperson.controller;


import com.example.rentaperson.dto.ApiResponse;
import com.example.rentaperson.dto.PostAppointment;
import com.example.rentaperson.model.Appointment;
import com.example.rentaperson.model.User;
import com.example.rentaperson.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/viewAll")
    public ResponseEntity<List> getAppointments(){
        List<Appointment> appointments=appointmentService.getAll();
        return ResponseEntity.status(200).body(appointments);
    }

    @GetMapping("/confirmed")
    public ResponseEntity<List> getConfirmedAppointments(@AuthenticationPrincipal User user){
        List<Appointment>  appointments=appointmentService.viewConfirm(user);
        return ResponseEntity.status(200).body(appointments);
    }

    @PostMapping("/post")
    public ResponseEntity<ApiResponse> addAppointment(@RequestBody PostAppointment ap, @AuthenticationPrincipal User user){
        appointmentService.post(ap,user.getId());
        return ResponseEntity.status(201).body(new ApiResponse("appointment added !",201));
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<ApiResponse> ConfirmAppointment(@PathVariable Integer id, @AuthenticationPrincipal User user){
        appointmentService.confirm(user,id);
        return ResponseEntity.status(201).body(new ApiResponse("appointment confirmed !",201));
    }

    @PutMapping("/update")
    public ResponseEntity updateApp(@RequestBody Appointment appointment){
        appointmentService.updateApp(appointment);
        return ResponseEntity.status(200).body("Appointment update");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteApp( @PathVariable Integer id){
        appointmentService.deleteApp(id);
        return ResponseEntity.status(201).body(new ApiResponse("Appointment deleted !",201));
    }

    @GetMapping("/myAppointments")
    public ResponseEntity<List> getPersonAppointments(@AuthenticationPrincipal User user){
        List<Appointment> appointments=appointmentService.viewYourAppointments(user);

        return ResponseEntity.status(200).body(appointments);
    }
}
