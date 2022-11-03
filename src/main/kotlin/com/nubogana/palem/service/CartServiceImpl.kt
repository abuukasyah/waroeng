package com.nubogana.palem.service

import com.nubogana.palem.model.Cart
import com.nubogana.palem.model.CartItem
import com.nubogana.palem.repository.CartRepository
import org.springframework.stereotype.Service

@Service
class CartServiceImpl(private val cartRepository: CartRepository) : CartService {

    override fun addCartItem(userId: String, cartItem: CartItem) {
        if (cartItem.product == null) {
            throw IllegalArgumentException("product can be null for cart.")
        }

        if (isProductAlreadyExist(userId, cartItem.product.sku)) {
            throw IllegalArgumentException("product already exist in cart.")
        }

        if (cartItem.quantity == null) {
            throw IllegalArgumentException("quantity can not be empty.")
        }

        this.cartRepository.addCartItem(userId, cartItem)
    }

    override fun isProductAlreadyExist(userId: String, sku: String): Boolean {
        return this.cartRepository.isProductAlreadyExist(userId, sku)
    }

    override fun listOfCarts(userId: String): Cart? {
        return this.cartRepository.findCartByUserId(userId)
    }

    override fun removeCartItem(userId: String, sku: String?) {
        if (sku.isNullOrEmpty()) {
            throw IllegalArgumentException("product can not be empty")
        }

        this.cartRepository.removeCartItem(userId, sku)
    }

    override fun updateCartItem(userid: String, cartItem: CartItem) {
        if (cartItem.product == null) {
            throw IllegalArgumentException("product can be null for cart.")
        }

        if (!isProductAlreadyExist(userid, cartItem.product.sku)) {
            throw IllegalArgumentException("product is not exist in cart.")
        }

        if (cartItem.quantity == null) {
            throw IllegalArgumentException("quantity can not be empty.")
        }

        this.cartRepository.updateCartItem(userid, cartItem)
    }
}
