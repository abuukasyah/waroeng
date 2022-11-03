package com.nubogana.palem.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nubogana.palem.model.Address
import com.nubogana.palem.model.Order
import com.nubogana.palem.model.OrderDetail
import com.nubogana.palem.model.Payment
import com.nubogana.palem.service.TimeUtil
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.ResultSet
import java.sql.Types

@Repository
@Transactional(readOnly = true)
class OrderRepositoryImpl(private val jdbcTemplate: NamedParameterJdbcTemplate) : OrderRepository {

    inner class OrderMapping : RowMapper<Order> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Order {
            return Order(
                rs.getString("id"),
                rs.getString("user_id"),
                jacksonObjectMapper().readValue(rs.getString("address"), Address::class.java),
                Payment.valueOf(rs.getString("payment")),
                rs.getString("notes"),
                BigDecimal(rs.getString("total")),
                mutableListOf(),
                rs.getString("order_status"),
                TimeUtil.parse(rs.getString("created_time"))
            )
        }
    }

    @Transactional(readOnly = false)
    override fun createOrder(order: Order) {
        val source = MapSqlParameterSource()
        source.addValue("user_id", order.userId)
        source.addValue("address", jacksonObjectMapper().writeValueAsString(order.address))
        source.addValue("payment", order.payment.toString())
        source.addValue("notes", order.notes)
        source.addValue("total", order.total)

        val insertOrder = """
            insert into palem."order" (user_id, address, payment, notes, total) 
            values (:user_id, :address, :payment, :notes, :total)
            returning palem."order".id;
        """.trimIndent()

        val parentId = this.jdbcTemplate.queryForObject(insertOrder, source, Long::class.java).toString()


        val sqlDetail = """
            insert into palem.order_detail ("order", sku, quantity)
            values (:order, :sku, :quantity); 
        """.trimIndent()

        val arr = arrayOfNulls<SqlParameterSource>(order.orderDetail?.size!!)
        for ((index, value) in order.orderDetail!!.withIndex()) {
            val map = MapSqlParameterSource()
            map.addValue("order", parentId, Types.BIGINT)
            map.addValue("sku", value.getSku())
            map.addValue("quantity", value.getQuantity())
            arr[index] = map
        }

        this.jdbcTemplate.batchUpdate(sqlDetail, arr)
    }

    override fun getOrder(userId: String): List<Order> {
        val sqlParent = "select * from palem.\"order\" po where user_id = :userId;"
        val source = MapSqlParameterSource("userId", userId)

        return this.jdbcTemplate.query(sqlParent, source, OrderMapping())
    }

    override fun isIdOrderExist(orderId: String): Boolean {
        val sql = """
            select exists(select 1 from palem."order" where id = :order_id);
        """.trimIndent()

        val source = MapSqlParameterSource()
        source.addValue("order_id", orderId, Types.BIGINT)
        return this.jdbcTemplate.queryForObject(sql, source, Boolean::class.java) ?: false
    }

    @Transactional(readOnly = false)
    override fun completeOrder(orderId: String) {
        val sql = """
            update palem."order" set order_status = :order_status where id = :order_id
        """.trimIndent()

        val source = MapSqlParameterSource()
        source.addValue("order_id", orderId, Types.BIGINT)
        source.addValue("order_status", Order.COMPLETED)
        this.jdbcTemplate.update(sql, source)
    }

    override fun getOrderDetails(userId: String, orderId: String): List<OrderDetail> {
        val sql = """
            select pod.* from palem.order_detail pod inner join palem."order" po on po.id = pod."order"
            where po.user_id = :user_id
        """.trimIndent()

        val source = MapSqlParameterSource()
        source.addValue("user_id", userId)
        source.addValue("order_id", orderId, Types.BIGINT)
        return this.jdbcTemplate.query(sql, source) { rs, _ ->
            OrderDetail(rs.getString("sku"), rs.getInt("quantity"))
        }
    }
}
