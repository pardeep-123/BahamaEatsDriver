package com.bahamaeatsdriver.model_class.store_menu


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("driver_menu_store")
    val driverMenuStore: String
)