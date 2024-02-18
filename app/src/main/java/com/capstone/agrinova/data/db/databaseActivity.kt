package com.capstone.agrinova.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HistoryDatabase"
        private const val TABLE_HISTORY = "history"
        private const val KEY_ID = "id"
        private const val KEY_IMAGE_PATH = "image_path"
        private const val KEY_ANALYSIS_RESULT = "analysis_result"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_HISTORY($KEY_ID INTEGER PRIMARY KEY, $KEY_IMAGE_PATH TEXT, $KEY_ANALYSIS_RESULT TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    fun addHistory(imagePath: String, analysisResult: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_IMAGE_PATH, imagePath)
        values.put(KEY_ANALYSIS_RESULT, analysisResult)
        db.insert(TABLE_HISTORY, null, values)
        db.close()
    }

    fun deleteHistory(imagePath: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_HISTORY, "$KEY_IMAGE_PATH = ?", arrayOf(imagePath))
        db.close()
        return result != -1
    }


    fun getAllHistory(): ArrayList<Pair<String, String>> {
        val historyList = ArrayList<Pair<String, String>>()
        val selectQuery = "SELECT * FROM $TABLE_HISTORY"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val imagePath = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH))
                val analysisResult = cursor.getString(cursor.getColumnIndex(KEY_ANALYSIS_RESULT))
                historyList.add(Pair(imagePath, analysisResult))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return historyList
    }

    // Tambahkan metode lain seperti menghapus, memperbarui, dan sebagainya sesuai kebutuhan.
}
