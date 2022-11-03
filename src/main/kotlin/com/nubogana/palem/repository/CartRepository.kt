package com.nubogana.palem.repository

import com.nubogana.palem.model.Cart
import com.nubogana.palem.model.CartItem

interface CartRepository {

    fun addCartItem(userId: String, cartItem: CartItem)

    fun findCartByUserId(userId: String): Cart?

    fun removeCartItem(userId: String, sku: String)

    fun updateCartItem(userid: String, cartItem: CartItem)

    fun isProductAlreadyExist(userId: String, sku: String): Boolean

    fun updateTotal(userId: String)
}
