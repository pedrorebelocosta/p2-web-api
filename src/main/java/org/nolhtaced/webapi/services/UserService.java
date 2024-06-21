package org.nolhtaced.webapi.services;

import lombok.RequiredArgsConstructor;
import org.nolhtaced.core.enumerators.AppointmentStateEnum;
import org.nolhtaced.core.enumerators.SellableTypeEnum;
import org.nolhtaced.core.enumerators.TransactionStateEnum;
import org.nolhtaced.core.exceptions.ProductNotFoundException;
import org.nolhtaced.core.exceptions.ServiceNotFoundException;
import org.nolhtaced.core.exceptions.UserNotFoundException;
import org.nolhtaced.core.models.*;
import org.nolhtaced.core.services.*;
import org.nolhtaced.webapi.models.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    public UserResponse getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

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

    public HttpStatus createUserAppointment(CreateAppointmentRequest appointmentBody) {
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
                            .scheduleDate(LocalDate.ofInstant(appointment.getScheduleDate(), ZoneId.of("UTC")))
                            .scheduleTime(LocalTime.ofInstant(appointment.getScheduleDate(), ZoneId.of("UTC")))
                            .notes(appointment.getCustomerNotes())
                            .state(appointment.getState())
                            .build()
                    )
                    .collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    public HttpStatus createUserBicycle(UserBikeBody bike) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        CustomerService userService = new CustomerService(null);

        try {

            Customer customer = userService.getByUsername(currentPrincipalName);
            Bicycle bicycle = new Bicycle(
                    customer.getId(),
                    bike.name(),
                    bike.model(),
                    bike.brand(),
                    bike.type(),
                    new ArrayList<>()
            );

            BicycleService bikeService = new BicycleService(null);
            bikeService.create(bicycle);
            return HttpStatus.CREATED;
        } catch (UserNotFoundException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public List<UserBikeBody> getAuthenticatedUserBikes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            CustomerService userService = new CustomerService(null);
            Customer customer = userService.getByUsername(username);

            return customer.getBicycles().stream().map(bicycle -> UserBikeBody.builder()
                    .name(bicycle.getName())
                    .brand(bicycle.getBrand())
                    .model(bicycle.getModel())
                    .type(bicycle.getType())
                    .build()
            ).collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    public HttpStatus createUserOrder(UserPurchaseLineItem[] items) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        ProductService productService = new ProductService(null);
        ServiceService serviceService = new ServiceService(null);
        TransactionService txService = new TransactionService(null);
        org.nolhtaced.core.services.UserService userService = new org.nolhtaced.core.services.UserService(null);


        List<ITransactionItem> txItems = Arrays.stream(items).<ITransactionItem>map((item) -> {
            if (item.type() == SellableTypeEnum.PRODUCT) {
                try {
                    Product product = productService.get(item.id());
                    return new TransactionItem(
                            item.id(),
                            product.getName(),
                            product.getTitle(),
                            item.qty(),
                            product.getPrice(),
                            item.type()
                    );
                } catch (ProductNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    org.nolhtaced.core.models.Service service = serviceService.get(item.id());
                    return new TransactionItem(
                            item.id(),
                            service.getName(),
                            service.getTitle(),
                            item.qty(),
                            service.getPrice(),
                            item.type()
                    );
                } catch (ServiceNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).toList();

        float total = 0F;

        for (ITransactionItem item : txItems) {
            total += item.getPrice() * item.getQuantity();
        }

        try {
            User customer = userService.getByUsername(username);
            txService.create(new Transaction(
                    customer.getId(),
                    null,
                    total,
                    LocalDate.now(),
                    TransactionStateEnum.ORDERED,
                    txItems
                    )
            );
            return HttpStatus.CREATED;
        } catch (UserNotFoundException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
