package com.bahamaeatsdriver.model_class.training_video_links


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("updatedAt")
    val updatedAt: Any
)