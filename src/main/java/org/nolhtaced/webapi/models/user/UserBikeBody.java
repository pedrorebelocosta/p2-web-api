package org.nolhtaced.webapi.models.user;

import lombok.Builder;
import org.nolhtaced.core.enumerators.BicycleTypeEnum;

@Builder
public record UserBikeBody(String name, String model, String brand, BicycleTypeEnum type) {}
