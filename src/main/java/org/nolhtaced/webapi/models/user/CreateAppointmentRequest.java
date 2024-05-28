package org.nolhtaced.webapi.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.nolhtaced.core.enumerators.AppointmentTypeEnum;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAppointmentRequest(
        AppointmentTypeEnum type,
        @JsonProperty("schedule_date") LocalDate scheduleDate,
        @JsonProperty("schedule_time") LocalTime scheduleTime,
        String notes
) { }
