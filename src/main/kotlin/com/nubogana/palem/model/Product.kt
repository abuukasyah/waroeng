package com.nubogana.palem.model

import java.math.BigDecimal
import java.time.Instant

data class Product(
    val id: String? = null,
    var category: Category? = null,
    val sku: String,
    val name: String? = null,
    val price: BigDecimal? = null,
    val description: String? = null,
    val weight: String? = null,
    val isAvailable: Boolean? = null,
    val image: String? = null,
    val createdTime: Instant? = Instant.now()
)
