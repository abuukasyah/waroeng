package com.nubogana.palem.service

import com.nubogana.palem.model.Order
import com.nubogana.palem.model.OrderDetail

interface OrderService {

    fun createOrder(order: Order)

    fun getOrder(userId: String): List<Order>

    fun complateOrder(order: Order)

    fun getOrderDetails(userId: String, orderId: String): List<OrderDetail>
}
