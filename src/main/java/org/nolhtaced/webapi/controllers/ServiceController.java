package org.nolhtaced.webapi.controllers;

import org.nolhtaced.core.exceptions.ServiceNotFoundException;
import org.nolhtaced.core.models.Service;
import org.nolhtaced.core.services.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {
    private final ServiceService service = new ServiceService(null);

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<Service> getService(@PathVariable Integer serviceId) {
        try {
            Service p = service.get(serviceId);
            return ResponseEntity.ok(p);
        } catch (ServiceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
