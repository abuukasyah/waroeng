package com.nubogana.palem.service

import com.nubogana.palem.model.Order
import com.nubogana.palem.model.OrderDetail
import com.nubogana.palem.repository.AddressRepository
import com.nubogana.palem.repository.CartRepository
import com.nubogana.palem.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val addressRepository: AddressRepository
) :
    OrderService {

    override fun createOrder(order: Order) {
        order.userId ?: throw IllegalArgumentException("please provide user id to checkout")

        this.cartRepository.findCartByUserId(order.userId)
            ?.also { order.total = it.total }
            ?.cartItems
            ?.also { order.orderDetail = OrderDetail.fromCartDetail(it) }
            ?: throw IllegalArgumentException("${order.userId} does not have a cart.")

        this.addressRepository.getUserAddress(order.userId)?.also { order.address = it }
            ?: throw IllegalArgumentException("${order.userId} does not have an address")

        this.orderRepository.createOrder(order)
    }

    override fun getOrder(userId: String): List<Order> {
        return this.orderRepository.getOrder(userId)
    }

    override fun complateOrder(order: Order) {
        if (!this.orderRepository.isIdOrderExist(order.id ?: "")) {
            throw IllegalArgumentException("order ID [${order.id}] is not exist.")
        }

        this.orderRepository.completeOrder(order.id!!)
    }

    override fun getOrderDetails(userId: String, orderId: String): List<OrderDetail> {
        if (!this.orderRepository.isIdOrderExist(orderId)) {
            throw IllegalArgumentException("order ID [${orderId}] is not exist.")
        }

        return this.orderRepository.getOrderDetails(userId, orderId)
    }
}
