package com.example.countersapp.data.api.models

import com.google.gson.annotations.SerializedName

data class CounterResponseModel(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("count") val count: Int
)