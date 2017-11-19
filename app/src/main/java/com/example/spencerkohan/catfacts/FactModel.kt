package com.example.spencerkohan.catfacts
import com.google.gson.annotations.SerializedName

data class FactModel (@SerializedName("fact") val fact : String,
                      @SerializedName("length") val length : Int)