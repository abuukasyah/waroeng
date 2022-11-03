package com.nubogana.palem.repository

import com.nubogana.palem.model.Category
import com.nubogana.palem.model.Product
import com.nubogana.palem.service.TimeUtil
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.ResultSet

@Repository
@Transactional(readOnly = true)
class ProductRepositoryImpl(private val jdbcTemplate: NamedParameterJdbcTemplate) : ProductRepository {

    inner class ProductMapper : RowMapper<Product> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Product {
            return Product(
                rs.getString("id"),
                Category(rs.getInt("category")),
                rs.getString("sku"),
                rs.getString("name"),
                BigDecimal(rs.getString("price")),
                rs.getString("description"),
                rs.getString("weight"),
                rs.getBoolean("is_available"),
                String(rs.getBytes("image")),
                TimeUtil.parse(rs.getString("created_time")),
            )
        }
    }

    override fun findProduct(sku: String): Product? {
        val sql = """
            select id, category, sku, name, price, description, weight, is_available, 
            image, created_time
            from palem.product where sku = :sku;
        """.trimIndent()
        return this.jdbcTemplate.query(
            sql,
            MapSqlParameterSource("sku", sku),
            ProductMapper()
        ).takeIf { it.size > 0 }
            ?.get(0)
    }

    override fun retrieveProducts(): List<Product> {
        val sql = """
            select id, category, sku, name, price, description, weight, is_available, 
            image, created_time
            from palem.product;
        """.trimIndent()
        return this.jdbcTemplate.query(sql, ProductMapper())
    }

    @Transactional(readOnly = false)
    override fun save(product: Product) {
        val sql = """
            insert into palem.product (category, name, sku, price, description, weight, is_available, image) 
             values (:category, :name, :sku, :price, :description, :weight, :isAvailable, :image)
        """.trimIndent()
        this.jdbcTemplate.update(sql, gerParameterSource(product))
    }

    @Transactional(readOnly = false)
    override fun remove(sku: String) {
        this.jdbcTemplate.update(
            "delete from palem.product where sku = :sku",
            MapSqlParameterSource("sku", sku)
        )
    }

    @Transactional(readOnly = false)
    override fun update(sku: String, product: Product) {
        val sql = """
            update palem.product set category = :category,
            sku = :sku,
            name = :name, 
            price = :price, 
            description = :description, 
            weight = :weight, 
            is_available = :isAvailable,
            image = :image where sku = :skuToUpdate
        """.trimIndent()
        val source = gerParameterSource(product) as MapSqlParameterSource
        source.addValue("skuToUpdate", sku)
        this.jdbcTemplate.update(sql, source)
    }

    fun gerParameterSource(product: Product): SqlParameterSource {
        val source = MapSqlParameterSource();
        source.addValue("sku", product.sku)
        source.addValue("category", product.category?.id)
        source.addValue("name", product.name)
        source.addValue("price", product.price)
        source.addValue("description", product.description)
        source.addValue("weight", product.weight)
        source.addValue("isAvailable", product.isAvailable)
        source.addValue("image", product.image?.toByteArray())

        return source
    }

    override fun searchProduct(search: String): List<Product> {
        val sql = """
            select id, category, sku, name, price, description, weight, is_available, 
            image, created_time from palem.product where 
            to_tsvector(sku) @@ to_tsquery(:localSearch)  
            or to_tsvector(name) @@ to_tsquery(:localSearch) 
            or to_tsvector(price) @@ to_tsquery(:localSearch)
            or to_tsvector(description) @@ to_tsquery(:localSearch);
        """.trimIndent()
        val localSearch = search.split(" ").joinToString(" | ")
        val source = MapSqlParameterSource("localSearch", localSearch)
        return this.jdbcTemplate.query(sql, source, ProductMapper())
    }
}
