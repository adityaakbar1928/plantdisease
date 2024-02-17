package com.capstone.agrinova.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommonDisease(
    val name : String,
    val description : String,
    val photo : Int
) : Parcelable
