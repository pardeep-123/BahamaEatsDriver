package com.bahamaeatsdriver.model_class


import com.google.gson.annotations.SerializedName

class ChatListModel : ArrayList<ChatListModel.ChatListModelItem>(){
    data class ChatListModelItem(
        @SerializedName("constantid")
        val constantid: Int, // 18
        @SerializedName("created_at")
        val createdAt: Long, // 1651833753
        @SerializedName("deleted_id")
        val deletedId: Int, // 0
        @SerializedName("id")
        val id: Int, // 165
        @SerializedName("message")
        val message: String, // hii
        @SerializedName("msg_type")
        val msgType: Int, // 0
        @SerializedName("read_status")
        val readStatus: Int, // 0
        @SerializedName("receiverFullName")
        val receiverFullName: String,
        @SerializedName("receiverId")
        val receiverId: Int, // 4
        @SerializedName("receiverImage")
        val receiverImage: String, // 8H69B8061ED97125F4G8HG53B50B.jpeg
        @SerializedName("receiverName")
        val receiverName: String, // deep singh
        @SerializedName("senderFullName")
        val senderFullName: String, // Dev jack
        @SerializedName("senderId")
        val senderId: Int, // 66
        @SerializedName("senderImage")
        val senderImage: String, // 0HD383D5759HKHG2ECE7B69F6BF2.jpg
        @SerializedName("senderName")
        val senderName: String,
        @SerializedName("updated_at")
        val updatedAt: String, // 2022-05-06T10:42:33.000Z
        @SerializedName("user2id")
        val user2id: Int, // 4
        @SerializedName("user2id_type")
        val user2idType: Int, // 1
        @SerializedName("userid")
        val userid: Int, // 66
        @SerializedName("userid_type")
        val useridType: Int // 2
    )
}