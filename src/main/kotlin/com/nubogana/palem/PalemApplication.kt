package com.nubogana.palem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@EnableJdbcRepositories
class PalemApplication

fun main(args: Array<String>) {
    runApplication<PalemApplication>(*args)
}
