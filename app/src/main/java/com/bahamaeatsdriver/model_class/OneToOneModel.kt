package com.bahamaeatsdriver.model_class
import com.google.gson.annotations.SerializedName


data class OneToOneModel(
    @SerializedName("body")
    val body: Body,
    @SerializedName("code")
    val code: Int, // 200
    @SerializedName("message")
    val message: String, // Message sent successfully.
    @SerializedName("success")
    val success: Boolean // true
) {
    data class Body(
        @SerializedName("constantid")
        val constantid: Int, // 16
        @SerializedName("created_at")
        val createdAt: Long, // 1651667032
        @SerializedName("deleted_id")
        val deletedId: Int, // 0
        @SerializedName("id")
        val id: Int, // 72
        @SerializedName("message")
        val message: String, // Hii user deep 
        @SerializedName("msg_type")
        val msgType: Int, // 0
        @SerializedName("read_status")
        val readStatus: Int, // 0
        @SerializedName("receiverName")
        val receiverName: String, // deep singh
        @SerializedName("reciverImage")
        val reciverImage: String, // B72C0KGEG6J7ED2E2E7828H97C9H.jpeg
        @SerializedName("senderImage")
        val senderImage: String, // 42DBCDG1KB4CFJF1J26KD7JEB16D.png
        @SerializedName("senderName")
        val senderName: String, // Rishab Kumar
        @SerializedName("updated_at")
        val updatedAt: String, // 2022-05-04T12:23:51.000Z
        @SerializedName("user2id")
        val user2id: Int, // 4
        @SerializedName("user2id_type")
        val user2idType: Int, // 1
        @SerializedName("userid")
        val userid: Int, // 8
        @SerializedName("userid_type")
        val useridType: Int // 2
    )
}