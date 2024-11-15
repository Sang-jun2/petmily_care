package com.example.petmily_care.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petmily_care.R
import com.example.petmily_care.model.ChatMessage

class ChatAdapter(private val messageList: MutableList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 사용자 메시지 ViewHolder
    class UserMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userIcon: ImageView = view.findViewById(R.id.user_icon)
        val userText: TextView = view.findViewById(R.id.user_text)
    }

    // 챗봇 메시지 ViewHolder
    class BotMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val botIcon: ImageView = view.findViewById(R.id.bot_icon)
        val botText: TextView = view.findViewById(R.id.bot_text)
    }

    override fun getItemViewType(position: Int): Int {
        // 사용자 메시지는 1, 챗봇 메시지는 2로 구분합니다.
        return if (messageList[position].isUser) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_message, parent, false)
            UserMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bot_message, parent, false)
            BotMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is UserMessageViewHolder) {
            holder.userText.text = message.content
            // 사용자 아이콘 설정 (이미지 리소스 수정 가능)
            holder.userIcon.setImageResource(R.drawable.user)
        } else if (holder is BotMessageViewHolder) {
            holder.botText.text = message.content
            // 챗봇 아이콘 설정 (이미지 리소스 수정 가능)
            holder.botIcon.setImageResource(R.drawable.chatbot)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // 메시지 추가 함수
    fun addMessage(message: ChatMessage) {
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }
}


