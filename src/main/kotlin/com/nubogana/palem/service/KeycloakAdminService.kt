package com.nubogana.palem.service

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nubogana.palem.model.User
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

@Component
class KeycloakAdminService(private val tokenService: TokenService) {

    companion object {
        const val userEndpoint = "http://localhost:8081/auth/admin/realms/palem/users"

        val client: HttpClient = HttpClient.newHttpClient()

        val mapper: ObjectMapper = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    fun getUser(userId: String): User {
        val request = HttpRequest.newBuilder()
            .uri(URI("$userEndpoint/$userId"))
            .header("Authorization", "Bearer ${this.tokenService.getAdminToken()}")
            .header("Content-Type", "application/json")
            .GET()
            .build()
        val responseString: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        val map: Map<String, String> = mapper.readValue(responseString.body())

        return User(
            map.getOrDefault("id", ""),
            map.getOrDefault("username", ""),
            map.getOrDefault("email", "")
        )
    }

    fun createUser(userPresentation: UserRepresentation): Boolean {
        val request = HttpRequest.newBuilder()
            .uri(URI(userEndpoint))
            .header("Authorization", "Bearer ${this.tokenService.getAdminToken()}")
            .header("Content-Type", "application/json")
            .header("Cache-Control", "no-cache")
            .POST(BodyPublishers.ofString(mapper.writeValueAsString(userPresentation)))
            .build()
        val responseString: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

        return responseString.statusCode() in 201..298
    }

    fun updateUser(userPresentation: UserRepresentation): Boolean {
        val request = HttpRequest.newBuilder()
            .uri(URI(userEndpoint))
            .header("Authorization", "Bearer ${this.tokenService.getAdminToken()}")
            .header("Content-Type", "application/json")
            .header("Cache-Control", "no-cache")
            .PUT(BodyPublishers.ofString(mapper.writeValueAsString(userPresentation)))
            .build()
        val responseString: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())

        return responseString.statusCode() in 201..298
    }
}
