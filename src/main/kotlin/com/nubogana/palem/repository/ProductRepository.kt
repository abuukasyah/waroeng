package com.nubogana.palem.repository

import com.nubogana.palem.model.Product

interface ProductRepository {

    fun findProduct(sku: String): Product?

    fun retrieveProducts(): List<Product>

    fun save(product: Product)

    fun remove(sku: String)

    fun update(sku: String, product: Product)

    fun searchProduct(search: String): List<Product>
}
