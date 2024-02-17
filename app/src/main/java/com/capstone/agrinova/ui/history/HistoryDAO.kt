package com.capstone.agrinova.ui.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDAO {
    @Insert
    fun insert(history: History)

    @Query("SELECT * FROM history_table")
    fun getAllHistory(): List<History>

    // Mungkin terdapat metode lainnya seperti update dan delete
}
