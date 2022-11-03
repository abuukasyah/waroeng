package com.nubogana.palem.service

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtil {

    fun parse(data: String?): Instant? {
        if (data.isNullOrEmpty()) {
            return null
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("UTC"))
            .parse(data)
        return Instant.from(formatter)
    }
}
