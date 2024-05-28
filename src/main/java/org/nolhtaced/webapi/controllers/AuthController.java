package org.nolhtaced.webapi.controllers;

import lombok.RequiredArgsConstructor;
import org.nolhtaced.webapi.models.auth.AuthResponse;
import org.nolhtaced.webapi.models.auth.SigninRequest;
import org.nolhtaced.webapi.models.auth.SignupRequest;
import org.nolhtaced.webapi.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody() SignupRequest req) {
        return ResponseEntity.ok(authenticationService.register(req));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody() SigninRequest req) {
        return ResponseEntity.ok(authenticationService.signin(req));
    }
}
