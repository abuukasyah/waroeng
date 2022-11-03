package com.nubogana.palem.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class IllegalArgExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    fun handleConflict(ex: RuntimeException?, request: WebRequest?): ResponseEntity<Any?>? {
        val bodyOfResponse = mapOf("error" to ex?.message, "code" to HttpStatus.BAD_REQUEST.value())
        return handleExceptionInternal(ex!!, bodyOfResponse, HttpHeaders(), HttpStatus.BAD_REQUEST, request!!)
    }
}
