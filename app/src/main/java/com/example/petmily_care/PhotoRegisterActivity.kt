package com.example.petmily_care

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petmily_care.database.DBHelper
import com.example.petmily_care.network.ApiService
import com.example.petmily_care.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoRegisterActivity : AppCompatActivity() {
    private var selectedImageUri: Uri? = null
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.photo_register_layout) // XML 레이아웃 설정

        dbHelper = DBHelper(this) // DBHelper 초기화

        // 선택된 이미지 URI 받아오기
        selectedImageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        val imageView = findViewById<ImageView>(R.id.registeredPhoto)
        selectedImageUri?.let {
            imageView.setImageURI(it)
        }

        // "진단하기" 버튼 클릭 리스너 설정
        findViewById<Button>(R.id.btnDiagnoseFromRegistered).setOnClickListener {
            selectedImageUri?.let { uri ->
                diagnoseImage(uri)
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
                R.id.nav_compare -> {
                    val intent = Intent(this, CompareActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun diagnoseImage(imageUri: Uri) {
        // 라디오 그룹을 통해 선택된 질환 유형 확인
        val diseaseTypeGroup = findViewById<RadioGroup>(R.id.disease_type_group)
        val selectedTypeId = diseaseTypeGroup.checkedRadioButtonId
        val diseaseType = when (selectedTypeId) {
            R.id.radio_eye -> "eye"
            R.id.radio_skin -> "skin"
            else -> {
                Toast.makeText(this, "질환 유형을 선택해 주세요.", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // 이미지 파일로 변환
        val file = File(imageUri.path ?: return)

        // 이미지 파일을 RequestBody 및 MultipartBody로 변환
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // 질환 유형을 RequestBody로 변환하여 추가 (서버에 질환 유형도 함께 전송)
        val diseaseTypeBody = diseaseType.toRequestBody("text/plain".toMediaTypeOrNull())

        // Retrofit을 사용하여 서버로 이미지 전송
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.uploadImage(body, diseaseTypeBody)

        // 서버 응답 처리
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()?.string()
                    result?.let {
                        parseAndDisplayResult(it)
                        saveDiagnosisToDatabase(it, imageUri.path ?: "")
                    }
                } else {
                    // 서버 응답 실패 처리
                    Toast.makeText(this@PhotoRegisterActivity, "서버에서 응답을 받지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 네트워크 요청 실패 처리
                Toast.makeText(this@PhotoRegisterActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseAndDisplayResult(result: String) {
        // 서버에서 받은 결과(JSON 문자열)를 파싱하고 화면에 표시
        val diseaseNameTextView = findViewById<TextView>(R.id.diagnosisResult)
        val diseaseDefinitionTextView = findViewById<TextView>(R.id.definition)
        val diseaseCauseTextView = findViewById<TextView>(R.id.cause)
        val hospitalExamTextView = findViewById<TextView>(R.id.hospitalExamination)
        val homeCareTextView = findViewById<TextView>(R.id.homeCareTips)

        try {
            val jsonObject = JSONObject(result)
            diseaseNameTextView.text = jsonObject.getString("질환")
            diseaseDefinitionTextView.text = jsonObject.getString("정의")
            diseaseCauseTextView.text = jsonObject.getString("원인")
            hospitalExamTextView.text = jsonObject.getString("병원 검사")
            homeCareTextView.text = jsonObject.getString("집에서의 관리법")
        } catch (e: JSONException) {
            Toast.makeText(this, "결과 파싱 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDiagnosisToDatabase(result: String, imagePath: String) {
        try {
            val jsonObject = JSONObject(result)
            val diseaseName = jsonObject.getString("질환")
            val cause = jsonObject.getString("원인")
            val diagnosisDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            dbHelper.insertDiagnosisRecord(imagePath, diagnosisDate, diseaseName, cause)
            Toast.makeText(this, "진단 이력이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: JSONException) {
            Toast.makeText(this, "DB 저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}


