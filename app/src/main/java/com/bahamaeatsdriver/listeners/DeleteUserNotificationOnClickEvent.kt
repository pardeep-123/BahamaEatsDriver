package com.bahamaeatsdriver.listeners

import com.bahamaeatsdriver.model_class.notification_listing.Body


interface DeleteUserNotificationOnClickEvent{
    fun  onClickDeleteUserNotifcation(postition:Int,notificationId:Int)
    fun  onClickReadUserNotifcation(postition:Int,notificationId:Int,notificationData: Body)
}