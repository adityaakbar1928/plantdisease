package com.capstone.agrinova.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.agrinova.databinding.CommonProblemsRowBinding
import com.capstone.agrinova.ui.CommonDetailActivity

class HomeFragmentAdapter(private val listCommon: ArrayList<CommonDisease>) : RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommonProblemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commonDisease = listCommon[position]
        holder.binding.tvItemName.text = commonDisease.name
        holder.binding.imgItemPhoto.setImageResource(commonDisease.photo)
        holder.itemView.setOnClickListener {
            val moveToDetail = Intent(holder.itemView.context, CommonDetailActivity::class.java)
            moveToDetail.putExtra(CommonDetailActivity.COMMON_DISEASE, commonDisease)
            holder.itemView.context.startActivity(moveToDetail)
        }
    }

    override fun getItemCount(): Int = listCommon.size

    class ViewHolder(var binding: CommonProblemsRowBinding) : RecyclerView.ViewHolder(binding.root)
}
