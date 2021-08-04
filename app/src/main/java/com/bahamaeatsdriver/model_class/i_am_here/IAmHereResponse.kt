package com.bahamaeatsdriver.model_class.i_am_here

data class IAmHereResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)