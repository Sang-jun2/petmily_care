package com.example.petmily_care

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PetDiseaseDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "pet_disease.db"
        private const val DATABASE_VERSION = 1

        // 테이블 및 열 정의
        const val TABLE_NAME = "disease_info"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DEFINITION = "definition"
        const val COLUMN_CAUSE = "cause"
        const val COLUMN_HOSPITAL_CHECK = "hospital_check"
        const val COLUMN_HOME_CARE = "home_care"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_DEFINITION TEXT,
                $COLUMN_CAUSE TEXT,
                $COLUMN_HOSPITAL_CHECK TEXT,
                $COLUMN_HOME_CARE TEXT
            )
        """.trimIndent()
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 데이터 삽입 메서드
    fun insertDiseaseInfo(name: String, definition: String, cause: String, hospitalCheck: String, homeCare: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DEFINITION, definition)
            put(COLUMN_CAUSE, cause)
            put(COLUMN_HOSPITAL_CHECK, hospitalCheck)
            put(COLUMN_HOME_CARE, homeCare)
        }
        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result != -1L // 성공적으로 삽입되면 -1이 아닌 값 반환
    }

    // 모든 데이터 조회 메서드
    fun getAllDiseases(): List<Map<String, String>> {
        val db = this.readableDatabase
        val diseaseList = mutableListOf<Map<String, String>>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val disease = mapOf(
                    COLUMN_NAME to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    COLUMN_DEFINITION to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEFINITION)),
                    COLUMN_CAUSE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAUSE)),
                    COLUMN_HOSPITAL_CHECK to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOSPITAL_CHECK)),
                    COLUMN_HOME_CARE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HOME_CARE))
                )
                diseaseList.add(disease)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return diseaseList
    }

    // 특정 데이터 삭제 메서드
    fun deleteDisease(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result > 0 // 삭제 성공 여부 반환
    }

    // 특정 데이터 업데이트 메서드
    fun updateDisease(id: Int, name: String, definition: String, cause: String, hospitalCheck: String, homeCare: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DEFINITION, definition)
            put(COLUMN_CAUSE, cause)
            put(COLUMN_HOSPITAL_CHECK, hospitalCheck)
            put(COLUMN_HOME_CARE, homeCare)
        }
        val result = db.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result > 0 // 업데이트 성공 여부 반환
    }
}
