package com.nubogana.palem.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CartItemDto(
    @JsonProperty("user_id") val userId: String,
    @JsonProperty("cart_item") val cartItem: CartItem
)
