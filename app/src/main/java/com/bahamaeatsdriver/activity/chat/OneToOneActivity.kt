package com.bahamaeatsdriver.activity.chat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Home_Page.Companion.loginDiverId
import com.bahamaeatsdriver.adapter.ChatAdapter
import com.bahamaeatsdriver.di.App
import com.bahamaeatsdriver.model_class.ChatListModel
import com.bahamaeatsdriver.model_class.OneToOneModel
import com.bahamaeatsdriver.socket.SocketManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_one_to_one.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class OneToOneActivity : AppCompatActivity(), SocketManager.Observer {
    val chatList = ArrayList<ChatListModel.ChatListModelItem>()
    var layoutManager: LinearLayoutManager? = null
    var user2Id = ""
    private var socketManager: SocketManager? = null
    private var activityScope = CoroutineScope(Dispatchers.Main)


    var chatAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_to_one)
        socketManager = App.getSocketManager()
        socketManager?.initializeSocket()
        user2Id = intent.getStringExtra("user2Id")!!
        ivSend.setOnClickListener {
            if (etMessage.text.toString().isEmpty()) {
                Toast.makeText(this, "write something", Toast.LENGTH_LONG).show()
            } else {
               // socketManager?.sendMessage(sendMessageAsJson())
            }
        }
        getChatListing()
        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        socketManager?.unRegister(this)
        socketManager?.onRegister(this)
    }

    // function for get chat listing
    private fun getChatListing() {
        val jsonObject = JSONObject()
        jsonObject.put("userid", loginDiverId)
        jsonObject.put("user2Id", user2Id)
        socketManager?.getChat(jsonObject)
    }

    /**
     * @author Pardeep Sharma
     * send message parameter
     * {
    "messageType":0, //0->text, 1->media
    "extension":"",
    "message":"Hii user deep",
    "userid":8,
    "user2Id":4,
    "userid_type":2, // 1->user 2->driver
    "user2id_type":1,  // 1->user 2->driver
    }
     */
    private fun sendMessageAsJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("userid", loginDiverId)
        jsonObject.put("user2Id", user2Id)
        jsonObject.put("extension", "")
        jsonObject.put("messageType", "0")
        jsonObject.put("userid_type", "2")
        jsonObject.put("user2id_type", "1")
        jsonObject.put("message", etMessage.text.toString().trim())
        return jsonObject
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        chatAdapter = ChatAdapter(this, chatList)
        rvChat.layoutManager = layoutManager

        /**
         * scroll screen to end when keyboard opens
         */

        rvChat.addOnLayoutChangeListener {
            _,_ ,_,_, bottom,_,_, _, oldBottom ->
            if (bottom < oldBottom) scrollToBottom()
        }
        rvChat.adapter = chatAdapter
    }

    private fun scrollToBottom() {
        layoutManager?.scrollToPosition(chatList.size - 1)
    }

    override fun onError(event: String?, vararg args: Any?) {

    }

    override fun onResponse(event: String?, vararg args: Any?) {
        Log.d("event is", event!!)
        if (event == SocketManager.SEND_MESSAGE_LISTENER) {
            val data = args[0] as JSONObject
            activityScope.launch {
                val gson = GsonBuilder().create()
                val list = gson.fromJson(
                    (data.get("body") as JSONObject).getString("message").toString(),
                    ChatListModel.ChatListModelItem::class.java
                )
                chatList.add(list)
                chatAdapter?.notifyDataSetChanged()
                rvChat.scrollToPosition(chatList.size - 1)

            }
        } else if (event == SocketManager.my_chat) {
            try {
                activityScope.launch {
                    val mObject = args[0] as JSONArray

                    val gson = GsonBuilder().create()
                    val listChatHistory =
                        gson.fromJson(mObject.toString(), ChatListModel::class.java)

                    chatList.clear()
                    chatList.addAll(listChatHistory)
                    Log.e("====chatListSize", chatList.size.toString())
//                    binding.tvUserName.text = userName
                    chatAdapter?.notifyDataSetChanged()
                    scrollToBottom()
                }
            } catch (e: Exception) {

            }
        }
    }
}