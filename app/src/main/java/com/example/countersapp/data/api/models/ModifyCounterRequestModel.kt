package com.example.countersapp.data.api.models

import com.google.gson.annotations.SerializedName

data class ModifyCounterRequestModel(
    @SerializedName("id") val id: String
)