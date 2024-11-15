package com.example.petmily_care.model

import android.net.Uri

data class DiagnosisRecord(
    val date: String,
    val diseaseName: String,
    val cause: String,
    val imageUri: String // 이미지 URI 필드 추가
)