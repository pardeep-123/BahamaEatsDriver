package com.bahamaeatsdriver.model_class.get_current_ride


import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @SerializedName("addons")
    val addons: String,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("itemDescription")
    val itemDescription: String,
    @SerializedName("itemId")
    val itemId: Int,
    @SerializedName("itemImage")
    val itemImage: String,
    @SerializedName("itemImagePath")
    val itemImagePath: String,
    @SerializedName("itemMenuId")
    val itemMenuId: Int,
    @SerializedName("itemName")
    val itemName: String,
    @SerializedName("itemSpecialRequest")
    val itemSpecialRequest: String,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("restaurantId")
    val restaurantId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)