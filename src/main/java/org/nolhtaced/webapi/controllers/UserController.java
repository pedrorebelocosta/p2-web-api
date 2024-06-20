package org.nolhtaced.webapi.controllers;

import lombok.RequiredArgsConstructor;
import org.nolhtaced.webapi.models.user.CreateAppointmentRequest;
import org.nolhtaced.webapi.models.user.UserAppointmentResponse;
import org.nolhtaced.webapi.models.user.UserBikeBody;
import org.nolhtaced.webapi.models.user.UserResponse;
import org.nolhtaced.webapi.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }

    @GetMapping("/me/bikes")
    public ResponseEntity<List<UserBikeBody>> myBikes() {
        return ResponseEntity.ok(userService.getAuthenticatedUserBikes());
    }

    @PostMapping("/me/bikes")
    public ResponseEntity<HttpStatus> addBikeToUser(@RequestBody UserBikeBody bike) {
        return ResponseEntity.status(userService.createUserBicycle(bike)).build();
    }

    @PostMapping("/me/appointments")
    public ResponseEntity<HttpStatus> createUserAppointment(@RequestBody CreateAppointmentRequest appointmentBody) {
        return ResponseEntity.status(userService.createUserAppointment(appointmentBody)).build();
    }

    @GetMapping("/me/appointments")
    public ResponseEntity<List<UserAppointmentResponse>> myAppointments() {
        return ResponseEntity.ok(userService.getAuthenticatedUserAppointments());
    }
}
