package com.nubogana.palem.repository

import com.nubogana.palem.model.Category
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface CategoriesRepository : PagingAndSortingRepository<Category, Int> {

    fun findByName(name: String): Category?

    fun existsByName(name: String): Boolean

    @Modifying
    @Query("delete from palem.category where name = :name")
    fun removeCategoryName(name: String)

    @Modifying
    @Query("update palem.category set name = :name where name = :oldName")
    fun updateWithName(name: String, oldName: String)
}
