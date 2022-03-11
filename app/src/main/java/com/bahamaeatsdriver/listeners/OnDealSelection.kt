package com.bahamaeatsdriver.listeners

import com.bahamaeatsdriver.model_class.driver_deals.All

interface OnDealSelection {
    fun OnDealSelection(isFav: All, position: Int, merchantId:String)
}