package com.nubogana.palem.controller

import com.nubogana.palem.model.Product
import com.nubogana.palem.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ProductController(private val productService: ProductService) {

    @GetMapping("/products")
    fun getProducts() = mapOf("data" to this.productService.findAllProducts(), "status" to getDefaultStatus())

    @GetMapping("/products/{sku}")
    fun getProduct(@PathVariable sku: String) = mapOf(
        "product" to this.productService.findProduct(sku), "status" to getDefaultStatus()
    )

    @GetMapping("/search")
    fun searchProduct(@RequestParam text: String) = mapOf("data" to this.productService.searchProduct(text))

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/products")
    fun createProduct(@RequestBody product: Product): ResponseEntity<out Map<String, Any>> {
        this.productService.saveProduct(product)
        return getDefaultStatus()
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/products/{sku}")
    fun removeProduct(@PathVariable sku: String): ResponseEntity<out Map<String, Any>> {
        this.productService.removeProduct(sku)
        return getDefaultStatus()
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/products/{sku}")
    fun updateProduct(@PathVariable sku: String, @RequestBody product: Product): ResponseEntity<out Map<String, Any>> {
        this.productService.updateProduct(sku, product)
        return getDefaultStatus()
    }

    fun getDefaultStatus(code: Int = 200, message: String = "success") =
        ResponseEntity.ok().body(mapOf("code" to code, "message" to message))
}
