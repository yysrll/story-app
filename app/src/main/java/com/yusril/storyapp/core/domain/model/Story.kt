package com.yusril.storyapp.core.domain.model

import com.google.gson.annotations.SerializedName

data class Story(
    var id : String,
    var name : String,
    var description : String,
    var photoUrl : String,
    var createdAt : String,
)
