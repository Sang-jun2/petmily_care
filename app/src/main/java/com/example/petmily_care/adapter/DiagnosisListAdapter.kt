package com.example.petmily_care.adapter

import android.net.Uri // Uri 클래스를 정확히 import
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petmily_care.R
import com.example.petmily_care.model.DiagnosisRecord

class DiagnosisListAdapter(private val diagnosisList: List<DiagnosisRecord>) :
    RecyclerView.Adapter<DiagnosisListAdapter.DiagnosisViewHolder>() {

    // ViewHolder 클래스 정의
    class DiagnosisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val registeredPhoto: ImageView = itemView.findViewById(R.id.registeredPhoto)
        val inspectionDate: TextView = itemView.findViewById(R.id.inspectionDate)
        val diseaseName: TextView = itemView.findViewById(R.id.diseaseName)
        val diseaseCause: TextView = itemView.findViewById(R.id.diseaseCause)
    }

    // onCreateViewHolder - ViewHolder를 초기화할 때 호출됨
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diagnosis_record, parent, false) // XML 레이아웃 수정
        return DiagnosisViewHolder(view)
    }

    // onBindViewHolder - 데이터와 ViewHolder를 연결할 때 호출됨
    override fun onBindViewHolder(holder: DiagnosisViewHolder, position: Int) {
        val record = diagnosisList[position]

        // 리소스 파일을 사용하여 문자열 형식을 정의
        val context = holder.itemView.context
        holder.inspectionDate.text = context.getString(R.string.inspection_date_format, record.date)
        holder.diseaseName.text = context.getString(R.string.disease_name_format, record.diseaseName)
        holder.diseaseCause.text = context.getString(R.string.disease_cause_format, record.cause)

        // 이미지 설정
        val imageUri = Uri.parse(record.imageUri) // String을 Uri로 변환
        holder.registeredPhoto.setImageURI(imageUri) // 이미지 뷰에 Uri 설정
    }

    // getItemCount - 아이템 개수 반환
    override fun getItemCount(): Int {
        return diagnosisList.size
    }
}

