package com.nubogana.palem.service

import com.nubogana.palem.model.Category
import com.nubogana.palem.model.PalemOperationException
import com.nubogana.palem.model.Product
import com.nubogana.palem.repository.CategoriesRepository
import com.nubogana.palem.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class ProductServiceImpl(
    private val categoriesRepository: CategoriesRepository, private val productRepository: ProductRepository
) : ProductService {

    override fun findAllCategory(): Map<String, Any> {
        return mapOf("categories" to this.categoriesRepository.findAll())
    }

    override fun findCategory(name: String): Category? = this.categoriesRepository.findByName(name)

    override fun saveCategory(category: Category) {
        val isAlreadyExist = this.categoriesRepository.existsByName(category.name)
        if (isAlreadyExist) {
            throw PalemOperationException("${category.name} already exist.", HttpStatus.BAD_REQUEST.value())
        }
        this.categoriesRepository.save(category)
    }

    override fun removeCategory(name: String) {
        val isAlreadyExist = this.categoriesRepository.existsByName(name)
        if (!isAlreadyExist) {
            throw PalemOperationException("$name is not exist to remove.", HttpStatus.BAD_REQUEST.value())
        }

        this.categoriesRepository.removeCategoryName(name)
    }

    override fun updateCategory(category: Category, name: String) {
        val isAlreadyExist = this.categoriesRepository.existsByName(category.name)
        if (isAlreadyExist) {
            throw PalemOperationException("${category.name} is not exist to update.", HttpStatus.BAD_REQUEST.value())
        }
        this.categoriesRepository.updateWithName(category.name, name)
    }

    override fun findAllProducts(): Map<String, Any> {
        return mapOf("products" to this.productRepository.retrieveProducts())
    }

    override fun findProduct(sku: String): Product? {
        val product = this.productRepository.findProduct(sku) ?: return null

        return product.also {
            val category = it.category?.id?.let { it1 -> this.categoriesRepository.findById(it1) }
            category.also {it2 -> it.category = it2?.get() }
        }
    }

    override fun saveProduct(product: Product) {
        val category = product.category?.let { this.categoriesRepository.findByName(it.name) }
            ?: throw IllegalArgumentException("product category is not valid : ${product.category}")

        product.category = category
        this.productRepository.save(product);
    }

    override fun removeProduct(sku: String) {
        this.productRepository.remove(sku);
    }

    override fun updateProduct(sku: String, product: Product) {
        this.productRepository.findProduct(sku) ?: throw IllegalArgumentException("$sku is not exist to update")
        val category = product.category?.let { this.categoriesRepository.findByName(it.name) }
            ?: throw IllegalArgumentException("product category is not valid : ${product.category}")
        product.category = category
        this.productRepository.update(sku, product)
    }

    override fun searchProduct(text: String): List<Product> {
        return this.productRepository.searchProduct(text)
    }
}
