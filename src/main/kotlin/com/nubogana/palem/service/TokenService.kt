package com.nubogana.palem.service

import com.fasterxml.jackson.module.kotlin.readValue
import com.nubogana.palem.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime

@Configuration
@PropertySource("classpath:keycloak-admin.properties")
class TokenService {

    companion object {
        const val adminTokenEndpoint = "http://localhost:8081/auth/realms/palem/protocol/openid-connect/token"

        const val userTokenEndpoint = "http://localhost:8081/auth/realms/palem/protocol/openid-connect/token"
    }

    @Value("\${grant_type}")
    private lateinit var grantType: String

    @Value("\${client_id}")
    private lateinit var clientId: String

    @Value("\${client_secret}")
    private lateinit var clientSecret: String

    private lateinit var expireDate: LocalDateTime

    private lateinit var token: String

    fun getAdminToken(): String {
        if (this::expireDate.isInitialized && LocalDateTime.now().isBefore(this.expireDate)) {
            return this.token
        }

        val body = "grant_type=$grantType&client_id=$clientId&client_secret=$clientSecret".toByteArray()
        val request = HttpRequest.newBuilder()
            .uri(URI(adminTokenEndpoint))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build()
        val responseString: HttpResponse<String> = KeycloakAdminService.client.send(request, HttpResponse.BodyHandlers.ofString())
        val map: Map<String, String> = KeycloakAdminService.mapper.readValue(responseString.body())

        this.expireDate = LocalDateTime.now().plusSeconds(map.getOrDefault("expires_in", "0").toLong())
        this.token = map.getOrDefault("access_token", "")

        return this.token;
    }

    fun generateUserToken(user: User): String {
        val body = "username=${user.username}&password=${user.password}&grant_type=password&client_id=palem-client".toByteArray()

        val request = HttpRequest.newBuilder()
            .uri(URI(userTokenEndpoint))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build()
        val responseString: HttpResponse<String> = KeycloakAdminService.client.send(request, HttpResponse.BodyHandlers.ofString())
        return responseString.body()
    }
}
