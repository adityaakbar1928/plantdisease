package com.capstone.agrinova.ui.history

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.agrinova.R
import com.capstone.agrinova.data.db.DatabaseHandler
import com.capstone.agrinova.databinding.ItemHistoryBinding

class HistoryAdapter(private val context: Context, private val historyList: List<Pair<String, String>>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<String, String>) {
            // Load image using Glide
            Glide.with(binding.root)
                .load(item.first) // Using URL from Pair to load image
                .placeholder(R.drawable.placeholder) // Placeholder image
                .error(R.drawable.error_image) // Image to display if error occurs
                .into(binding.imageViewHistory) // Load image into ImageView
            binding.textDiagnoseResult.text = item.second
            // Set click listener on image view
            binding.imageViewHistory.setOnClickListener {
                // Perform action when image is clicked (e.g., enlarge image)
                // You can implement your custom logic here
                showImageDialog(item.first)
            }

            // Set click listener on delete button
            binding.buttonDelete.setOnClickListener {
                // Perform action when delete button is clicked (e.g., delete data)
                // You can implement your custom logic here
                showDeleteConfirmationDialog()
            }
        }

        private fun showImageDialog(imagePath: String) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_image)

            val imageView = dialog.findViewById<ImageView>(R.id.imageViewDialog)
            Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(imageView)

            val closeButton = dialog.findViewById<ImageButton>(R.id.buttonClose)
            closeButton.setOnClickListener {
                dialog.dismiss() // Close the dialog when the button is clicked
            }

            dialog.show()
        }
        private fun showDeleteConfirmationDialog() {
            AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete") { dialog, _ ->
                    // Perform delete operation here
                    deleteItemFromDatabase(position)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        private fun deleteItemFromDatabase(position: Int) {
            val databaseHandler = DatabaseHandler(context)
            val selectedItem = historyList[position]
            val isSuccess = databaseHandler.deleteHistory(selectedItem.first)
            if (isSuccess) {
                // Hapus item dari daftar setelah berhasil menghapus dari database
                historyList.toMutableList().removeAt(position)
                Toast.makeText(context, "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
                notifyItemRemoved(position)
            } else {
                Toast.makeText(context, "Gagal menghapus item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
