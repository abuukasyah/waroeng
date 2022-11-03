package com.nubogana.palem.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Address(
    var id: String? = null,
    @JsonProperty("user_id") val userId: String? = null,
    val province: String? = null,
    val city: String? = null,
    @JsonProperty("sub_district") val subDistrict: String? = null,
    @JsonProperty("urban_village") val urbanVillage: String? = null
)
