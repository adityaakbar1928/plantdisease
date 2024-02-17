package com.capstone.agrinova.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.agrinova.databinding.ActivityCommonDetailBinding
import com.capstone.agrinova.databinding.FragmentHomeBinding
import com.capstone.agrinova.utils.CommonDisease

class CommonDetailActivity : AppCompatActivity() {
    private var _binding: ActivityCommonDetailBinding? = null
    private val binding get() = _binding!!

    companion object{
        const val COMMON_DISEASE = "common_disease"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val disease = if(Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(COMMON_DISEASE, CommonDisease::class.java)
        } else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(COMMON_DISEASE)
        }

        if (disease != null){
            binding.ivCommonDetail.setImageResource(disease.photo)
            binding.tvCommonDetailName.text = disease.name
            binding.tvCommonDetailDeskripsi.text = disease.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}