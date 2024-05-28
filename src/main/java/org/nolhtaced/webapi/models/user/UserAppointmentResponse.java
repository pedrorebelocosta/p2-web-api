package org.nolhtaced.webapi.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.nolhtaced.core.enumerators.AppointmentStateEnum;
import org.nolhtaced.core.enumerators.AppointmentTypeEnum;

import java.time.LocalDate;

@Builder
public record UserAppointmentResponse(
        @JsonProperty("created_at") LocalDate createdAt,
        AppointmentTypeEnum type,
        String notes,
        AppointmentStateEnum state
) { }
