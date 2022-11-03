package com.nubogana.palem.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.Instant

data class Order(
    val id: String? = null,
    @JsonProperty("user_id") val userId: String?,
    var address: Address?,
    val payment: Payment?,
    val notes: String?,
    var total: BigDecimal?,
    @JsonProperty("order_detail") var orderDetail: List<OrderDetail>?,
    @JsonProperty("order_status") val orderStatus: String = IN_PROGRESS,
    @JsonProperty("created_time") val createdTime: Instant?
) {
    companion object {
        const val IN_PROGRESS = "IN_PROGRESS"
        const val COMPLETED = "COMPLETED"
    }
}
