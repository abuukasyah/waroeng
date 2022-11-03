package com.nubogana.palem.repository

import com.nubogana.palem.model.Address
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class AddressRepositoryImpl(private val jdbcTemplate: NamedParameterJdbcTemplate) : AddressRepository {

    inner class AddressMapper : RowMapper<Address> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Address {
            return Address(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("province"),
                rs.getString("city"),
                rs.getString("sub_district"),
                rs.getString("urban_village")
            )
        }
    }

    override fun updateOrInsertAddress(address: Address): Address {
        val sql = "insert into palem.address (user_id, province, city, sub_district, urban_village) " +
                "values (:userId, :province, :city, :sub_district, :urban_village) " +
                "on conflict do nothing " +
                "returning id;"

        val source = MapSqlParameterSource()
        source.addValue("userId", address.userId)
        source.addValue("province", address.province)
        source.addValue("city", address.city)
        source.addValue("sub_district", address.subDistrict)
        source.addValue("urban_village", address.urbanVillage)

        val returningId = this.jdbcTemplate.queryForObject(sql, source, Int::class.java)
        address.id = returningId.toString()

        return address
    }

    override fun getUserAddress(userId: String): Address? {
        val sql = """
            select * from palem.address where user_id = :user_id;
        """.trimIndent()

        return this.jdbcTemplate.queryForObject(sql, MapSqlParameterSource("user_id", userId), AddressMapper())
    }

    override fun isUserAddressExist(userId: String): Boolean {
        val sql = """
            select exists(select 1 from palem.address where user_id = :user_id)
        """.trimIndent()

        return this.jdbcTemplate.queryForObject(
            sql,
            MapSqlParameterSource("user_id", userId), Boolean::class.java
        ) ?: false
    }
}
