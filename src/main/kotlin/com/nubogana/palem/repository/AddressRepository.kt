package com.nubogana.palem.repository

import com.nubogana.palem.model.Address

interface AddressRepository {

    fun updateOrInsertAddress(address: Address): Address

    fun getUserAddress(userId: String): Address?

    fun isUserAddressExist(userId: String): Boolean
}
