package com.example.petmily_care

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CompareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compare_layout) // list_layout.xml 사용

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
                R.id.nav_compare -> {
                    val intent = Intent(this, CompareActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}