package com.nubogana.palem.service

import com.nubogana.palem.model.User
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val keycloakAdminService: KeycloakAdminService, private val tokenService: TokenService) : UserService {

    override fun findUser(userId: String): User {
        return this.keycloakAdminService.getUser(userId)
    }

    override fun createUser(user: User): Boolean {
        return this.keycloakAdminService.createUser(createKeycloakEntity(user))
    }

    override fun updateUser(user: User): Boolean {
        return this.keycloakAdminService.updateUser(createKeycloakEntity(user))
    }

    private fun createKeycloakEntity(user: User): UserRepresentation {
        val credentialRepresentation = CredentialRepresentation()
        credentialRepresentation.type = "password"
        credentialRepresentation.value = user.password
        credentialRepresentation.isTemporary = false

        val userEntity = UserRepresentation()
        userEntity.username = user.username
        userEntity.email = user.email
        userEntity.isEnabled = true
        userEntity.firstName = user.firstName
        userEntity.lastName = user.lastName
        val attr = mapOf("dateOfBirth" to listOf(user.dateOfBirth.toString()),
            "telephoneNumber" to listOf(user.telephoneNumber))
        userEntity.attributes = attr
        if (user.password != null) {
            userEntity.credentials = listOf(credentialRepresentation)
        }

        return userEntity
    }

    override fun getUserToken(user: User): String {
        return this.tokenService.generateUserToken(user)
    }
}
