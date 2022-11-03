package com.nubogana.palem.repository

import com.nubogana.palem.model.Cart
import com.nubogana.palem.model.CartItem
import com.nubogana.palem.model.Product
import com.nubogana.palem.service.TimeUtil
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(readOnly = true)
class CartRepositoryImpl(private val jdbcTemplate: NamedParameterJdbcTemplate) : CartRepository {

    inner class CartMapping : RowMapper<Cart> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Cart {
            return Cart(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getBigDecimal("total"),
                TimeUtil.parse(rs.getString("created_time"))
            )
        }
    }

    inner class CartItemsMapping : RowMapper<CartItem> {
        override fun mapRow(rs: ResultSet, rowNum: Int): CartItem {
            return CartItem(
                rs.getString("id"),
                Product(sku = rs.getString("sku")),
                rs.getInt("quantity"),
                TimeUtil.parse(rs.getString("created_time"))
            )
        }
    }

    @Transactional(readOnly = false)
    override fun addCartItem(userId: String, cartItem: CartItem) {
        val source = MapSqlParameterSource()
        source.addValue("user_id", userId)
        source.addValue("sku", cartItem.product?.sku)
        source.addValue("quantity", cartItem.quantity)

        val sqlCart = """
            with cart_cte as (
                insert into palem.cart (user_id) values ('12345678') on conflict (user_id) do nothing returning id
            ) select id from cart_cte
            union all
            select id from palem.cart where user_id = '12345678'
            limit 1
        """.trimIndent()
        val cartId = this.jdbcTemplate.queryForObject(sqlCart, source, Int::class.java)
        source.addValue("cart", cartId)

        val sqlItems = """
            insert into palem.cart_items (cart, sku, quantity) values (:cart, :sku, :quantity) 
            on conflict (cart, sku, quantity) do update set quantity = :quantity;
        """.trimIndent()

        this.jdbcTemplate.update(sqlItems, source)

        updateTotal(userId)
    }

    override fun isProductAlreadyExist(userId: String, sku: String): Boolean {
        val source = MapSqlParameterSource()
        source.addValue("userId", userId)
        source.addValue("sku", sku)
        return this.jdbcTemplate.queryForObject(
            """
                select exists(select * from palem.cart_items ci
                inner join cart c on c.id = ci.cart where c.user_id = :userId and sku = :sku)
            """.trimIndent(),
            source,
            Boolean::class.java
        ) ?: false
    }

    override fun findCartByUserId(userId: String): Cart? {
        val sqlCart = "select * from palem.cart where user_id = :user_id;"
        val cart = this.jdbcTemplate.queryForObject(
            sqlCart,
            MapSqlParameterSource("user_id", userId), CartMapping()
        )

        val sqlCartItem = """
            select items.* from palem.cart_items items 
            inner join cart c on c.id = items.cart where c.user_id = :user_id;
        """.trimIndent()
        val cartItems: List<CartItem> = this.jdbcTemplate.query(
            sqlCartItem,
            MapSqlParameterSource("user_id", userId),
            CartItemsMapping()
        )

        if (cart != null) {
            cart.cartItems = cartItems
        }

        return cart
    }

    @Transactional(readOnly = false)
    override fun removeCartItem(userId: String, sku: String) {
        val sql = """
            delete from palem.cart_items items using palem.cart c 
            where c.id = items.cart and c.user_id = :user_id and items.sku = :sku;
        """.trimIndent()

        val source = MapSqlParameterSource()
        source.addValue("user_id", userId)
        source.addValue("sku", sku)

        this.jdbcTemplate.update(sql, source)

        updateTotal(userId)
    }

    @Transactional(readOnly = false)
    override fun updateCartItem(userid: String, cartItem: CartItem) {
        val sqlItems = """
            update palem.cart_items items set quantity = :quantity 
            from palem.cart c where c.user_id = :user_id and c.id = items.cart and sku = :sku;
        """.trimIndent()
        val source = MapSqlParameterSource()
        source.addValue("user_id", userid)
        source.addValue("sku", cartItem.product?.sku)
        source.addValue("quantity", cartItem.quantity)

        this.jdbcTemplate.update(sqlItems, source)

        updateTotal(userid)
    }

    override fun updateTotal(userId: String) {
        val sqlTotal = """
           update palem.cart c
           set total = coalesce(( select sum(ci.quantity::int * p.price::numeric)
                         from cart_items ci
                                  inner join product p on p.sku = ci.sku ), 0)
           where c.user_id = :user_id;
       """.trimIndent()
        this.jdbcTemplate.update(sqlTotal, MapSqlParameterSource("user_id", userId))
    }
}
