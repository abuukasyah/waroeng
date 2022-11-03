package com.nubogana.palem.model

import java.time.Instant

data class CartItem(val id: String? = null, val product: Product?, val quantity: Int?, val createdTime: Instant? = null)
