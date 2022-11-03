package com.nubogana.palem.controller

import com.nubogana.palem.model.Cart
import com.nubogana.palem.model.CartItemDto
import com.nubogana.palem.service.CartService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class CartController(private val cartService: CartService) {

    @PreAuthorize("hasRole('customer')")
    @PostMapping("/carts")
    fun addItem(@RequestBody cartItemDto: CartItemDto): Map<String, Any> {
        this.cartService.addCartItem(cartItemDto.userId, cartItemDto.cartItem)
        return mapOf("status" to "success", "code" to 200)
    }

    @PreAuthorize("hasRole('customer')")
    @GetMapping("/carts/{userId}")
    fun getCarts(@PathVariable userId: String): Map<String, Cart?> {
        return mapOf("carts" to this.cartService.listOfCarts(userId))
    }

    @PreAuthorize("hasRole('customer')")
    @DeleteMapping("/carts")
    fun removeItem(@RequestBody cartItemDto: CartItemDto): Map<String, Any> {
        this.cartService.removeCartItem(cartItemDto.userId, cartItemDto.cartItem.product?.sku)
        return mapOf("status" to "success", "code" to 200)
    }

    @PreAuthorize("hasRole('customer')")
    @PutMapping("/carts")
    fun updateItem(@RequestBody cartItemDto: CartItemDto): Map<String, Any> {
        this.cartService.updateCartItem(cartItemDto.userId, cartItemDto.cartItem)
        return mapOf("status" to "success", "code" to 200)
    }
}
