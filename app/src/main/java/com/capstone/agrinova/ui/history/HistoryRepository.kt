package com.capstone.agrinova.ui.history

import android.content.Context

class HistoryRepository(context: Context) {
    private val historyDao: HistoryDAO

    init {
        val database = HistoryDatabase.getDatabase(context)
        historyDao = database.historyDao()
    }

    suspend fun insert(history: History) {
        historyDao.insert(history)
    }

    suspend fun getAllHistory(): List<History> {
        return historyDao.getAllHistory()
    }
}