package com.example.petmily_care

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.petmily_care.ui.theme.Petmily_careTheme
import android.graphics.Bitmap
import android.util.Log
import java.io.File

class MainActivity : ComponentActivity() {

    // 사진 촬영 결과를 받을 수 있도록 ActivityResultLauncher 선언
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        // 사진 촬영 후의 bitmap 이미지 처리
        if (bitmap != null) {
            val file = File(cacheDir, "captured_image.png")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Log.d("MainActivity", "Image saved at ${file.absolutePath}")
        }
    }

    // 카메라 권한 요청을 처리할 ActivityResultLauncher 선언
    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // 권한이 허용되면 카메라 열기
            openCamera()
        } else {
            // 권한이 거부된 경우 사용자에게 알림 처리 추가 가능
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidView(
                factory = { context ->
                    val view = LayoutInflater.from(context).inflate(R.layout.head_layout, null as ViewGroup?, false)

                    // "사진 등록" 버튼 클릭 리스너 설정
                    view.findViewById<Button>(R.id.button_register).setOnClickListener {
                        val intent = Intent(this, PhotoRegisterActivity::class.java)
                        startActivity(intent)
                    }

                    // "사진 촬영" 버튼 클릭 리스너 설정
                    view.findViewById<Button>(R.id.button_capture).setOnClickListener {
                        // 카메라 권한 확인 및 요청
                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }

                    view
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    private fun openCamera() {
        // 카메라를 열어 사진을 찍고 결과를 받음
        takePictureLauncher.launch(null)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Petmily_careTheme {
        Greeting("Android")
    }
}
