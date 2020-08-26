package com.example.countersapp.data.api.models

import com.google.gson.annotations.SerializedName

data class CreateCounterRequestModel(
    @SerializedName("title") val title: String
)