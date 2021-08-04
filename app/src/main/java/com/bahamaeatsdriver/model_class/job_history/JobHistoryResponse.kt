package com.bahamaeatsdriver.model_class.job_history

data class JobHistoryResponse(
    val body: Body,
    val code: Int,
    val message: String,
    val status: Boolean
)