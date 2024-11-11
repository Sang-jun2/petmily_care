package com.example.petmily_care.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.petmily_care.model.DiagnosisRecord

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "petmily_care.db"
        private const val TABLE_NAME = "DiagnosisHistory"
        private const val COLUMN_ID = "id"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_DIAGNOSIS_DATE = "diagnosis_date"
        private const val COLUMN_DISEASE_NAME = "disease_name"
        private const val COLUMN_CAUSE = "cause"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_IMAGE_PATH TEXT, " +
                "$COLUMN_DIAGNOSIS_DATE TEXT, " +
                "$COLUMN_DISEASE_NAME TEXT, " +
                "$COLUMN_CAUSE TEXT)")
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 데이터를 삽입하는 메서드 추가
    fun insertDiagnosisRecord(imagePath: String, diagnosisDate: String, diseaseName: String, cause: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_IMAGE_PATH, imagePath)
        values.put(COLUMN_DIAGNOSIS_DATE, diagnosisDate)
        values.put(COLUMN_DISEASE_NAME, diseaseName)
        values.put(COLUMN_CAUSE, cause)

        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    // 모든 진단 기록을 가져오는 메서드 추가
    fun getAllDiagnosisRecords(): List<DiagnosisRecord> {
        val diagnosisRecords = mutableListOf<DiagnosisRecord>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $COLUMN_DIAGNOSIS_DATE, $COLUMN_DISEASE_NAME, $COLUMN_CAUSE FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val diagnosisDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIAGNOSIS_DATE))
                val diseaseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISEASE_NAME))
                val cause = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAUSE))

                val diagnosisRecord = DiagnosisRecord(
                    date = diagnosisDate,
                    diseaseName = diseaseName,
                    cause = cause
                )
                diagnosisRecords.add(diagnosisRecord)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return diagnosisRecords
    }
}
