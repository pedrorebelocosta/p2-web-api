package org.nolhtaced.webapi.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.nolhtaced.core.enumerators.AppointmentStateEnum;
import org.nolhtaced.core.enumerators.AppointmentTypeEnum;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record UserAppointmentResponse(
        @JsonProperty("created_at") LocalDate createdAt,
        @JsonProperty("schedule_date") LocalDate scheduleDate,
        @JsonProperty("schedule_time") LocalTime scheduleTime,
        AppointmentTypeEnum type,
        String notes,
        AppointmentStateEnum state
) { }
