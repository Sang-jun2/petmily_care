package com.example.petmily_care.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 기존의 이미지 업로드 API
    @Multipart
    @POST("upload")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("diseaseType") diseaseType: RequestBody
    ): Call<ResponseBody>

    // 채팅 메시지 전송 및 응답 받기
    @FormUrlEncoded
    @POST("chat")
    fun sendMessage(
        @Field("message") message: String
    ): Call<String>
}
