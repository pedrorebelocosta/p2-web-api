package org.nolhtaced.webapi.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponse(
        String username,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("date_of_birth") LocalDate dateOfBirth,
        @JsonProperty("discount_rate") Float discountRate,
        String address,
        @JsonProperty("vat_no") Integer vatNo
) { }
