package com.yusril.storyapp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    var id : String,
    var name : String,
    var description : String,
    var photoUrl : String,
    var createdAt : String,
    var lat: String = "0",
    var lon: String = "0"
) : Parcelable
