package com.example.petmily_care.model

data class ChatMessage(
    val content: String,
    val isUser: Boolean // 사용자의 메시지인지 챗봇의 메시지인지 여부를 나타냅니다.
)