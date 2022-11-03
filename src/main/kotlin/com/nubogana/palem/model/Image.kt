package com.nubogana.palem.model

import java.time.LocalDateTime

data class Image(val product: Product, val path: String, val createdTime: LocalDateTime)