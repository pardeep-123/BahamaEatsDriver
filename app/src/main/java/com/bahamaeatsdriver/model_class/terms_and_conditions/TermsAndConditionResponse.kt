package com.bahamaeatsdriver.model_class.terms_and_conditions

data class TermsAndConditionResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)