package com.capstone.agrinova.data.response

import com.google.gson.annotations.SerializedName

data class AgrinovaResponse(

	@field:SerializedName("prediction")
	val prediction: String
)
