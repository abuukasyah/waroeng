package com.nubogana.palem.model

import org.springframework.data.annotation.Id
import java.time.Instant

data class Category(
    @Id
    val id: Int? = null,
    val name: String,
    val createdTime: Instant? = Instant.now(),
) {
    constructor(id: Int) : this(id, "")
}
