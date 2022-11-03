package com.nubogana.palem.model

data class OrderDetail(private val sku: String? = null, private val quantity: Int? = null) {

    companion object {
        fun fromCartDetail(cartItem: List<CartItem>): List<OrderDetail> {
            return cartItem.map { OrderDetail(it.product?.sku, it.quantity) }
        }
    }

    fun getSku(): String? {
        return this.sku
    }

    fun getQuantity(): Int? {
        return this.quantity
    }
}
