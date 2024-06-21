package org.nolhtaced.webapi.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserPurchaseRequest(
        @JsonProperty("cart_items") UserPurchaseLineItem[] items
) { }
