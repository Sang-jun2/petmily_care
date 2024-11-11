package com.example.petmily_care

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

import android.graphics.Bitmap
import android.util.Log
import java.io.File

class MainActivity : ComponentActivity() {

    private var selectedImageUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val file = File(cacheDir, "captured_image.png")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Log.d("MainActivity", "Image saved at ${file.absolutePath}")
            selectedImageUri = Uri.fromFile(file)
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                selectedImageUri = imageUri
                Log.d("MainActivity", "Image selected: $imageUri")
            }
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
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

                    view.findViewById<Button>(R.id.button_register).setOnClickListener {
                        val intent = Intent(this, PhotoRegisterActivity::class.java)
                        selectedImageUri?.let {
                            intent.putExtra("imageUri", it.toString())
                        }
                        startActivity(intent)
                    }

                    view.findViewById<Button>(R.id.button_capture).setOnClickListener {
                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }

                    view.findViewById<Button>(R.id.button_select).setOnClickListener {
                        openGallery()
                    }

                    view
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    private fun openCamera() {
        takePictureLauncher.launch(null)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
}

