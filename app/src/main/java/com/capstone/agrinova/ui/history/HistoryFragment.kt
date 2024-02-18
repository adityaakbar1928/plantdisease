package com.capstone.agrinova.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.agrinova.data.db.DatabaseHandler
import com.capstone.agrinova.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var databaseHandler: DatabaseHandler

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi database handler
        databaseHandler = DatabaseHandler(requireContext())

        // Inisialisasi RecyclerView dan atur layout manager
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Mendapatkan data riwayat dari database
        val historyList = databaseHandler.getAllHistory().filter { it.first.isNotBlank() && it.second.isNotBlank() }

        // Buat instance adapter RecyclerView dengan menggunakan data riwayat
        historyAdapter = HistoryAdapter(requireContext(), historyList)

        // Setel adapter ke RecyclerView untuk menampilkan data riwayat
        binding.recyclerView.adapter = historyAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
