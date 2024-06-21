package org.nolhtaced.webapi.models.user;

import org.nolhtaced.core.enumerators.SellableTypeEnum;

public record UserPurchaseLineItem(Integer id, SellableTypeEnum type, Float qty) { }
