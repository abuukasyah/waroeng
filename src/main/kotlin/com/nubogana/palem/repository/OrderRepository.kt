package com.nubogana.palem.repository

import com.nubogana.palem.model.Order
import com.nubogana.palem.model.OrderDetail

interface OrderRepository {

    fun createOrder(order: Order)

    fun getOrder(userId: String): List<Order>

    fun isIdOrderExist(orderId: String): Boolean

    fun completeOrder(orderId: String)

    fun getOrderDetails(userId: String, orderId: String) : List<OrderDetail>
}
