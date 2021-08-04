package com.bahamaeatsdriver.listeners

import com.bahamaeatsdriver.model_class.slots.AvailableSlot

interface OnDriverAvailabilitySelection {
    fun OnDriverAvailabilitySelection(item: AvailableSlot, slotPosition: Int, dayPosition:Int)
}