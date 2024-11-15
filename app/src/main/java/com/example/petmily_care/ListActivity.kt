package com.example.petmily_care

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petmily_care.database.DBHelper
import com.example.petmily_care.model.DiagnosisRecord
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.petmily_care.adapter.DiagnosisListAdapter

class ListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var diagnosisListAdapter: DiagnosisListAdapter

    // 액티비티의 onCreate에서 설정하는 부분
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosis_record)

        // DBHelper 인스턴스 초기화
        dbHelper = DBHelper(this)

        // 데이터베이스에서 진단 기록 불러오기
        val diagnosisRecords: List<DiagnosisRecord> = dbHelper.getAllDiagnosisRecords()

        // RecyclerView 설정
        val recyclerView = findViewById<RecyclerView>(R.id.diagnosisRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // RecyclerView 어댑터 설정
        diagnosisListAdapter = DiagnosisListAdapter(diagnosisRecords)
        recyclerView.adapter = diagnosisListAdapter

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
                R.id.nav_chatbot-> {
                    val intent = Intent(this, ChatbotActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}

