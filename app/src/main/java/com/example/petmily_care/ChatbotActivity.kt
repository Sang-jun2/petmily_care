package com.example.petmily_care

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petmily_care.adapter.ChatAdapter
import com.example.petmily_care.model.ChatMessage
import com.example.petmily_care.network.ApiService
import com.example.petmily_care.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatbotActivity : AppCompatActivity() {

    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatbot_layout)

        // View 초기화
        chatInput = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.sendButton)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)

        // RecyclerView 설정
        chatAdapter = ChatAdapter(messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        // 버튼 클릭 리스너 설정
        sendButton.setOnClickListener {
            val userMessage = chatInput.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                // 사용자가 입력한 메시지를 리스트에 추가하고 RecyclerView에 표시
                val userChatMessage = ChatMessage(content = userMessage, isUser = true)
                messageList.add(userChatMessage)
                chatAdapter.notifyItemInserted(messageList.size - 1)
                chatRecyclerView.scrollToPosition(messageList.size - 1)  // 새 메시지가 보이도록 스크롤
                sendMessageToServer(userMessage)
                chatInput.text.clear()
            }
        }

        // 네비게이션 바 항목 선택 리스너 설정
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_diagnose -> {
                    val intent = Intent(this, PhotoRegisterActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_history -> {
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_chatbot -> {
                    val intent = Intent(this, ChatbotActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun sendMessageToServer(message: String) {
        // Retrofit 인스턴스 생성 및 ApiService 사용
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        val call = apiService.sendMessage(message)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val botResponse = response.body() ?: "응답이 없습니다."
                    // 서버에서 받은 응답을 리스트에 추가하고 RecyclerView에 표시
                    val botChatMessage = ChatMessage(content = botResponse, isUser = false)
                    messageList.add(botChatMessage)
                    chatAdapter.notifyItemInserted(messageList.size - 1)
                    chatRecyclerView.scrollToPosition(messageList.size - 1)  // 새 메시지가 보이도록 스크롤
                } else {
                    Toast.makeText(
                        this@ChatbotActivity,
                        "응답 실패: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    this@ChatbotActivity,
                    "오류 발생: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

