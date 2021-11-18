package com.bahamaeatsdriver.model_class.training_video_links

import com.google.gson.annotations.SerializedName

data class TrainingVideoLinksResponse(
    @SerializedName("body")
    val body: List<Body>,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class Body(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)