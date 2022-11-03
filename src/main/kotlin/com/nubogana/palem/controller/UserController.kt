package com.nubogana.palem.controller

import com.nubogana.palem.model.User
import com.nubogana.palem.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController(private val userService: UserService) {

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/users/{user-id}")
    fun findUser(@PathVariable("user-id") userId: String): Map<String, User> {
        return mapOf("user" to this.userService.findUser(userId))
    }

    @PostMapping("/users")
    fun createUser(@RequestBody user: User): Map<String, Boolean> {
        return mapOf("status" to this.userService.createUser(user))
    }

    @PreAuthorize("hasRole('customer')")
    @PutMapping("/users")
    fun updateUser(@RequestBody user: User): Map<String, Boolean> {
        return mapOf("status" to this.userService.updateUser(user))
    }

    @PostMapping("/users/token")
    fun getUserToken(@RequestBody user: User): String {
        return this.userService.getUserToken(user)
    }
}
