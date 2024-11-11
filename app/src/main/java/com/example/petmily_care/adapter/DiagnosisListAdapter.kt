package com.example.petmily_care.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petmily_care.model.DiagnosisRecord
import com.example.petmily_care.R

class DiagnosisListAdapter(private val diagnosisList: List<DiagnosisRecord>) :
    RecyclerView.Adapter<DiagnosisListAdapter.DiagnosisViewHolder>() {

    // ViewHolder 클래스 정의
    class DiagnosisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inspectionDate: TextView = itemView.findViewById(R.id.inspectionDate)
        val diseaseName: TextView = itemView.findViewById(R.id.diseaseName)
        val diseaseCause: TextView = itemView.findViewById(R.id.diseaseCause)
    }

    // onCreateViewHolder - ViewHolder를 초기화할 때 호출됨
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_layout, parent, false)
        return DiagnosisViewHolder(view)
    }

    // onBindViewHolder - 데이터와 ViewHolder를 연결할 때 호출됨
    override fun onBindViewHolder(holder: DiagnosisViewHolder, position: Int) {
        val record = diagnosisList[position]
        holder.inspectionDate.text = record.date
        holder.diseaseName.text = record.diseaseName
        holder.diseaseCause.text = record.cause
    }

    // getItemCount - 아이템 개수 반환
    override fun getItemCount(): Int {
        return diagnosisList.size
    }
}
