package org.nolhtaced.webapi.controllers;

import lombok.RequiredArgsConstructor;
import org.nolhtaced.webapi.models.user.CreateAppointmentRequest;
import org.nolhtaced.webapi.models.user.UserAppointmentResponse;
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
    public ResponseEntity<UserResponse> me(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader) {
        return ResponseEntity.ok(userService.getAuthenticatedUser(authHeader));
    }

    @PostMapping("/me/appointments")
    public ResponseEntity<HttpStatus> createUserAppointment(
            @RequestBody CreateAppointmentRequest appointmentBody,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        return ResponseEntity.status(userService.createUserAppointment(appointmentBody, authHeader)).build();
    }

    @GetMapping("/me/appointments")
    public ResponseEntity<List<UserAppointmentResponse>> myAppointments() {
        return ResponseEntity.ok(userService.getAuthenticatedUserAppointments());
    }
}
