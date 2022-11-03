package com.nubogana.palem.service

import com.nubogana.palem.model.Cart
import com.nubogana.palem.model.CartItem

interface CartService {

    fun addCartItem(userId: String, cartItem: CartItem)

    fun isProductAlreadyExist(userId: String, sku: String): Boolean

    fun listOfCarts(userId: String): Cart?

    fun removeCartItem(userId: String, sku: String?)

    fun updateCartItem(userid: String, cartItem: CartItem)
}
