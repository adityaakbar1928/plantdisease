package com.capstone.agrinova.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.agrinova.R
import com.capstone.agrinova.databinding.FragmentHomeBinding
import com.capstone.agrinova.utils.CommonDisease
import com.capstone.agrinova.utils.HomeFragmentAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val listCommonDisease = ArrayList<CommonDisease>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listCommonDisease.addAll(getListCommonProblems())
        showRecyclerList()
    }

    private fun getListCommonProblems(): ArrayList<CommonDisease> {
        val dataName = resources.getStringArray(R.array.common_problems_name)
        val dataDescription = resources.getStringArray(R.array.common_problems_desc)
        val dataPhoto = resources.obtainTypedArray(R.array.common_photo)
        val listCommon = ArrayList<CommonDisease>()
        for (i in dataName.indices) {
            val commonDisease = CommonDisease(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listCommon.add(commonDisease)
        }
        return listCommon
    }

    fun showRecyclerList(){
        binding.rvCommonProblems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCommonProblems.adapter = HomeFragmentAdapter(listCommonDisease)
        binding.rvCommonProblems.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}