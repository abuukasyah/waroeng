package com.nubogana.palem.model

data class PalemOperationException(val mesg: String, val code: Int) : RuntimeException(mesg)
