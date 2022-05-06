package com.bahamaeatsdriver.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bahamaeatsdriver.R
import kotlinx.android.synthetic.main.activity_one_to_one.*

class ChatListActivity : AppCompatActivity() {
//    val chatList= ArrayList<String>()
//    var layoutManager: LinearLayoutManager?=null
//
//    val chatAdapter:ChatAdapter by lazy {
//        ChatAdapter(this,chatList)
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

//        setAdapter()
    }

//    private fun setAdapter() {
//        layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
//        rvChat.layoutManager= layoutManager
//        rvChat.setHasFixedSize(true)
//        rvChat.adapter=chatAdapter
//    }
}