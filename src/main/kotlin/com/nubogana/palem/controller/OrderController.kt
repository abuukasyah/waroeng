package com.nubogana.palem.controller

import com.nubogana.palem.model.Order
import com.nubogana.palem.service.OrderService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class OrderController(private val orderService: OrderService) {

    @PreAuthorize("hasRole('customer')")
    @PostMapping("/orders")
    fun createOrder(@RequestBody order: Order): Map<String, Any> {
        this.orderService.createOrder(order)
        return mapOf("status" to "success", "code" to 200)
    }

    @PreAuthorize("hasRole('customer')")
    @PutMapping("/orders")
    fun completeOrder(@RequestBody order: Order): Map<String, Any> {
        this.orderService.complateOrder(order)
        return mapOf("status" to "success", "code" to 200)
    }

    @PreAuthorize("hasRole('customer')")
    @GetMapping("/orders/{userId}")
    fun getOrders(@PathVariable userId: String): Map<String, Any> {
        return mapOf("orders" to this.orderService.getOrder(userId), "code" to "200")
    }

    @PreAuthorize("hasRole('customer')")
    @GetMapping("/orders/details/{userId}/{order_id}")
    fun getOrderDetails(@PathVariable userId: String, @PathVariable("order_id") orderId: String): Map<String, Any> {
        return mapOf("orders" to this.orderService.getOrderDetails(userId, orderId), "code" to "200")
    }
}
