package com.capstone.agrinova.ui.camera

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.capstone.agrinova.databinding.FragmentCameraFeatureBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import com.capstone.agrinova.data.db.DatabaseHandler

class CameraFeatureFragment : Fragment() {

    private var _binding: FragmentCameraFeatureBinding? = null
    private val binding get() = _binding!!
    private var imageUrl: String? = null
    private lateinit var resultTextView: TextView


    private var bitmap: Bitmap? = null

    private val cameraPermission = android.Manifest.permission.CAMERA
    private val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val mediaPermission = android.Manifest.permission.ACCESS_MEDIA_LOCATION

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cameraFeatureViewModel =
            ViewModelProvider(this).get(CameraFeatureViewModel::class.java)

        _binding = FragmentCameraFeatureBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardCamera.setOnClickListener { useCamera() }
        binding.cardGallery.setOnClickListener { openGallery() }
        binding.diagnoseButton.setOnClickListener { uploadImageToServer() }
        resultTextView = binding.resultTextView


        // Set the initial visibility of diagnose_button to invisible
        binding.diagnoseButton.visibility = View.INVISIBLE
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                bitmap = imageBitmap
                Glide.with(binding.previewImageView).load(imageBitmap)
                    .into(binding.previewImageView)

                // Make the diagnose_button visible after capturing the image
                binding.diagnoseButton.visibility = View.VISIBLE
            }
        }

    private val documentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val contentResolver = requireActivity().contentResolver
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            bitmap = imageBitmap
            Glide.with(binding.previewImageView).load(imageBitmap).into(binding.previewImageView)

            // Make the diagnose_button visible after selecting the image from the gallery
            binding.diagnoseButton.visibility = View.VISIBLE
        }

    fun uploadImageToServer(): String? {
        val url_image = "http://103.159.20.238:3000/upload"
        var hasilprediksi: String? = null
        binding.loadingProgressBar.visibility = View.VISIBLE

        bitmap?.let { imageBitmap ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            val base64WithoutLineBreaks = encodedString.replace("\n", "").replace("\r", "")

            val params = JSONObject()
            try {
                params.put("image", base64WithoutLineBreaks)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val request = JsonObjectRequest(
                Request.Method.POST, url_image, params,
                { response ->
                    try {
                        val imageUrl = response.getString("imageUrl")
                        Log.d("Response", "imageUrl: $imageUrl")
                        requestDataset(imageUrl)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error -> Log.e("Error", error.message!!) }
            )


            val requestQueue_image = Volley.newRequestQueue(context)
            requestQueue_image.add<JSONObject>(request)
        } ?: run {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
        }

        return imageUrl // Mengembalikan imageUrl
    }

    private fun requestDataset(imageUrl: String?) {
        imageUrl?.let { url ->
            val MODEL_ENDPOINT = "plants-diseases-detection-and-classification/12"
            val API_KEY = "x924C39CcE5KiuNEjBhG"
            val url_dataset = "https://detect.roboflow.com/$MODEL_ENDPOINT?api_key=$API_KEY&image=$url"
            val params_dataset = JSONObject()
            try {
                params_dataset.put("Content-Type", "application/x-www-form-urlencoded")
                params_dataset.put("Content-Language", "en-US")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val request = JsonObjectRequest(
                Request.Method.POST, url_dataset, params_dataset,
                { response ->
                    try {
                        val hasilprediksi = response.getJSONArray("predictions")
                        for (i in 0 until hasilprediksi.length()) {
                            // Dapatkan objek prediksi
                            val predictionObject = hasilprediksi.getJSONObject(i)
                            // Dapatkan nilai "class" dari objek prediksi
                            val prediksi_kelas = predictionObject.getString("class")
                            // Lakukan sesuatu dengan nilai "class", misalnya mencetaknya
                            Log.d("Prediksi", "Class: $prediksi_kelas")
                            resultTextView.text = "Hasil Analisa: $prediksi_kelas"
                            binding.loadingProgressBar.visibility = View.GONE
                            // Memanggil fungsi addHistory dengan hasil prediksi kelas
                            val controlDB = DatabaseHandler(requireContext())
                            controlDB.addHistory(url, prediksi_kelas)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error -> Log.e("Error", error.message!!) }
            )

            val requestQueue_dataset = Volley.newRequestQueue(context)
            requestQueue_dataset.add<JSONObject>(request)
        }
    }



        fun useCamera() {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    cameraPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(cameraPermission),
                    6
                )
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(intent)
            }
        }

        fun openGallery() {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    storagePermission
                ) != PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    mediaPermission
                )) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(storagePermission, mediaPermission),
                    4
                )
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                documentLauncher.launch(intent.type)
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }