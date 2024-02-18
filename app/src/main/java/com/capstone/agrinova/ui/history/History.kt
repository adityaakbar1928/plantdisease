package com.capstone.agrinova.ui.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String,
    val date: Long
)