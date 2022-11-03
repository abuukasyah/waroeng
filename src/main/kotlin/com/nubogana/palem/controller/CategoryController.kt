package com.nubogana.palem.controller

import com.nubogana.palem.model.Category
import com.nubogana.palem.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class CategoryController(private val productService: ProductService) {

    @GetMapping("/categories")
    fun getCategories() = mapOf("data" to this.productService.findAllCategory(), "status" to getDefaultStatus())

    @GetMapping("/categories/{name}")
    fun getCategory(@PathVariable name: String) = mapOf(
        "category" to this.productService.findCategory(name), "status" to getDefaultStatus()
    )

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/categories")
    fun createCategory(@RequestBody category: Category): ResponseEntity<out Map<String, Any>> {
        this.productService.saveCategory(category)
        return getDefaultStatus()
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/categories")
    fun removeCategory(@RequestBody category: Category): ResponseEntity<out Map<String, Any>> {
        this.productService.removeCategory(category.name)
        return getDefaultStatus()
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/categories/{name}")
    fun updateCategory(
        @RequestBody category: Category,
        @PathVariable name: String
    ): ResponseEntity<out Map<String, Any>> {
        this.productService.updateCategory(category, name)
        return getDefaultStatus()
    }

    fun getDefaultStatus(code: Int = 200, message: String = "success") =
        ResponseEntity.ok().body(mapOf("code" to code, "message" to message))
}
