package com.bahamaeatsdriver.model_class.faqs

import com.google.gson.annotations.SerializedName


data class Body(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    var isSelected: Int
)