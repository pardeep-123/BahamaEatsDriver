package com.bahamaeatsdriver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeats.constant.Constants
import com.bahamaeatsdriver.R
import com.bahamaeatsdriver.activity.Home_Page
import com.bahamaeatsdriver.helper.extensions.getPrefrence
import com.bahamaeatsdriver.helper.others.CommonMethods
import com.bahamaeatsdriver.model_class.ChatListModel
import com.bahamaeatsdriver.model_class.OneToOneModel
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
    val context: Context,
    internal val arrayList: ArrayList<ChatListModel.ChatListModelItem>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_USER = 0
    val TYPE_FRIEND = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
// apitom..
        if (viewType == TYPE_USER) {
            return (UserViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_view_my_chat, parent, false)
            ))
        } else if (viewType == TYPE_FRIEND) {
            return (FriendViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_view_friend_chat, parent, false
                )
            ))
        }

        return (FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_friend_chat,
                parent,
                false
            )
        ))

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == TYPE_FRIEND) {

            val friendViewHolder = holder as FriendViewHolder
            if (arrayList[position].msgType == 1) {
                friendViewHolder.txt_friendView.visibility = View.GONE
                friendViewHolder.image.visibility = View.VISIBLE
                Glide.with(context).load(Constants.SOCKET_IMAGE_BASE_URL+arrayList[position].message).into(friendViewHolder.image)

            }else{
                friendViewHolder.txt_friendView.visibility = View.VISIBLE
                friendViewHolder.image.visibility = View.GONE
                friendViewHolder.txt_friendView.text = arrayList[position].message
            }

            friendViewHolder.txt_time.text =
                CommonMethods.convertTimeStampToDa(arrayList[position].createdAt)
            Glide.with(context)
                .load("https://dev.bahamaeats.com:8008/images/drivers/" + arrayList[position].receiverImage)
                .into(friendViewHolder.profileimage)

        } else if (holder.itemViewType == TYPE_USER) {
            val userViewHolder = holder as UserViewHolder
            userViewHolder.txt_userView.text = arrayList[position].message
            Glide.with(context)
                .load("https://dev.bahamaeats.com:8008/images/users/" + arrayList[position].senderImage)
                .into(userViewHolder.profileimage)
            userViewHolder.txt_time.text =
                CommonMethods.convertTimeStampToDa(arrayList[position].createdAt)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val data = arrayList[position]

        if (data.senderId.toString() == Home_Page.loginDiverId) {
            return TYPE_USER
        } else {
            return TYPE_FRIEND
        }
        // return 0 // remove this line while uncomment above
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_userView: TextView = itemView.findViewById(R.id.tvMyMsg)
        val txt_time: TextView = itemView.findViewById(R.id.tvTime)
        val profileimage: CircleImageView = itemView.findViewById(R.id.ivUser)
//        val image: ImageView = itemView.findViewById(R.id.ivSendPic)
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_friendView: TextView = itemView.findViewById(R.id.tvFriendMsg)
        val txt_time: TextView = itemView.findViewById(R.id.tvFrndMsgTime)
        val profileimage: CircleImageView = itemView.findViewById(R.id.ivFriend)
        val image: ImageView = itemView.findViewById(R.id.ivSendPic)
    }
}
