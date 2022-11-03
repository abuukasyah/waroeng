package com.nubogana.palem.service

import com.nubogana.palem.model.Category
import com.nubogana.palem.model.Product

interface ProductService {

    fun findAllCategory(): Map<String, Any>

    fun findCategory(name: String): Category?

    fun saveCategory(category: Category)

    fun removeCategory(name: String)

    fun updateCategory(category: Category, name: String)

    fun findAllProducts(): Map<String, Any>

    fun findProduct(sku: String): Product?

    fun saveProduct(product: Product)

    fun removeProduct(sku: String)

    fun updateProduct(sku: String, product: Product)

    fun searchProduct(text: String): List<Product>
}
