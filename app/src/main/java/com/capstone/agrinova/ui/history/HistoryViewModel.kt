package com.capstone.agrinova.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.agrinova.data.db.DatabaseHandler

class HistoryViewModel(private val databaseHandler: DatabaseHandler) : ViewModel() {

    private val _historyList = MutableLiveData<List<Pair<String, String>>>()
    val historyList: LiveData<List<Pair<String, String>>>
        get() = _historyList

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _historyList.value = databaseHandler.getAllHistory()
    }
}