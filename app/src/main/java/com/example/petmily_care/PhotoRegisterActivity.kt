package com.example.petmily_care

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PhotoRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.photo_register_layout) // XML 레이아웃 설정
    }
}
