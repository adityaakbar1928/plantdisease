package com.capstone.agrinova.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.agrinova.R

class HistoryAdapter(private val historyList: List<Pair<String, String>>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePathTextView: TextView = itemView.findViewById(R.id.textView)
        val analysisResultTextView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.imagePathTextView.text = currentItem.first
        holder.analysisResultTextView.text = currentItem.second
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
