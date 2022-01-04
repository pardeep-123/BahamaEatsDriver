package com.bahamaeatsdriver.listeners

import com.bahamaeatsdriver.model_class.driver_deals.Body

interface OnDealSelection {
    fun OnDealSelection(isFav: Body, position: Int, merchantId:String)
}