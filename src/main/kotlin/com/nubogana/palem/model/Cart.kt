package com.nubogana.palem.model

import java.math.BigDecimal
import java.time.Instant

data class Cart(
    val id: String? = null,
    val userId: String,
    val total: BigDecimal? = null,
    val createdTime: Instant? = null,
    var cartItems: List<CartItem>? = null,
)
