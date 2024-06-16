package org.nolhtaced.webapi.models.user;

import lombok.Builder;
import org.nolhtaced.core.enumerators.BicycleTypeEnum;

@Builder
public record UserBikeResponse(String name,  String model, String brand, BicycleTypeEnum type) {}
