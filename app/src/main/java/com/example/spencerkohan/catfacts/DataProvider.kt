package com.example.spencerkohan.catfacts

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class DataProvider(var context: Context, var observer: Observer) {

    private val requestQueue: RequestQueue
    private val responseObserver: Observer
    private val gson = Gson()

    interface Observer {
        fun didReceiveData(newFacts:List<FactModel>)
    }

    init {
        requestQueue = Volley.newRequestQueue(context)
        responseObserver = observer
    }

    var factLength: Int = 100

    fun fetchNextPage() {

        val query = "limit=10&max_length=" + factLength

        val request = JsonObjectRequest(Request.Method.GET,  "https://catfact.ninja/facts?" + query, JSONObject(),
                Response.Listener<JSONObject> { response ->

                    val factsJson = response.getJSONArray("data")

                    val factListType = object : TypeToken<List<FactModel>>() {}.type
                    val page : List<FactModel> = gson.fromJson<List<FactModel>>(factsJson.toString(), factListType)

                    responseObserver.didReceiveData(page)

                },Response.ErrorListener { error ->
                    Log.e("API", "/post request fail! Error: ${error.message}")
        })

        requestQueue.add(request)

    }

}