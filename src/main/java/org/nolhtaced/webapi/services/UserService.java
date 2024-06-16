package org.nolhtaced.webapi.services;

import lombok.RequiredArgsConstructor;
import org.nolhtaced.core.enumerators.AppointmentStateEnum;
import org.nolhtaced.core.exceptions.UserNotFoundException;
import org.nolhtaced.core.models.Appointment;
import org.nolhtaced.core.models.Customer;
import org.nolhtaced.core.services.AppointmentService;
import org.nolhtaced.core.services.CustomerService;
import org.nolhtaced.webapi.models.user.CreateAppointmentRequest;
import org.nolhtaced.webapi.models.user.UserAppointmentResponse;
import org.nolhtaced.webapi.models.user.UserBikeResponse;
import org.nolhtaced.webapi.models.user.UserResponse;
import org.nolhtaced.webapi.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;

    public UserResponse getAuthenticatedUser(String authHeader) {
        String authToken = authHeader.substring(7);
        String username = jwtService.extractUsername(authToken);

        try {
            Customer customer = new CustomerService(null).getByUsername(username);

            return UserResponse.builder()
                    .username(customer.getUsername())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .dateOfBirth(customer.getDateOfBirth())
                    .discountRate(customer.getDiscountRate())
                    .address(customer.getAddress())
                    .vatNo(customer.getVatNo())
                    .build();
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpStatus createUserAppointment(
            CreateAppointmentRequest appointmentBody,
            String authHeader
    ) {
        AppointmentService service = new AppointmentService(null);
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentBody.scheduleDate(), appointmentBody.scheduleTime());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        CustomerService userService = new CustomerService(null);

        try {
            Customer customer = userService.getByUsername(currentPrincipalName);
            Appointment newAppointment = new Appointment(
                    LocalDate.now(),
                    appointmentDateTime.toInstant(ZoneOffset.UTC),
                    appointmentBody.type(),
                    customer.getId(),
                    customer.getId(),
                    appointmentBody.notes(),
                    "",
                    AppointmentStateEnum.PENDING
            );
            service.create(newAppointment);
            return HttpStatus.CREATED;
        } catch (UserNotFoundException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public List<UserAppointmentResponse> getAuthenticatedUserAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            CustomerService userService = new CustomerService(null);
            Customer customer = userService.getByUsername(username);
            return new AppointmentService(null).getAll()
                    .stream()
                    .filter(appointment -> appointment.getCustomerId().equals(customer.getId()))
                    .map(appointment -> UserAppointmentResponse.builder()
                            .createdAt(appointment.getCreatedAt())
                            .type(appointment.getType())
                            .notes(appointment.getCustomerNotes())
                            .state(appointment.getState())
                            .build()
                    )
                    .collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    public List<UserBikeResponse> getAuthenticatedUserBikes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            CustomerService userService = new CustomerService(null);
            Customer customer = userService.getByUsername(username);

            return customer.getBicycles().stream().map(bicycle -> UserBikeResponse.builder()
                    .name(bicycle.getName())
                    .brand(bicycle.getBrand())
                    .model(bicycle.getBrand())
                    .type(bicycle.getType())
                    .build()
            ).collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            return null;
        }
    }
}
