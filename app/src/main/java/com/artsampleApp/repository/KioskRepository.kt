package com.artsampleApp.repository

import android.content.Context
import com.artsampleApp.models.Kiosk
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import org.json.JSONObject
import java.lang.Exception

object KioskRepository {

    private val url = "https://api.automatedrt.com/kiosks/brand/2/nearby"
    val kiosks = mutableListOf<Kiosk>()
    fun loadKiosks(context : Context, lat: Double, long:Double, distance: Int, completion:(kiosks: List<Kiosk>,isSuccessful: Boolean)->Unit)
    {

        val requestJSON = JSONObject()
        requestJSON.put("latitude",lat)
        requestJSON.put("longitude",long)
        requestJSON.put("distance",distance)

        val request = JsonObjectRequest(Request.Method.POST, url,requestJSON,object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject?) {
                var isSuccessful = false
                try {
                    kiosks.clear()
                    val kioskJSONArray = response?.getJSONArray("results")
                    if (kioskJSONArray != null) {
                        for (i in 0 until kioskJSONArray.length()) {
                            val jsonObject = kioskJSONArray.getJSONObject(i)
                            val kiosk = Kiosk(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("identifier"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("lastReported"),
                                    jsonObject.getString("updated"),
                                    jsonObject.getString("address"),
                                    jsonObject.getString("serial"),
                                    jsonObject.getBoolean("online"),
                                    jsonObject.getBoolean("active")
                            )
                            kiosks.add(kiosk)
                        }
                        isSuccessful = true
                    }
                }
                catch (ex: Exception)
                {
                    ex.printStackTrace()
                }

                completion(kiosks,isSuccessful)
            }


        },
        object: ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                completion(emptyList(),false)
            }

        })

        Volley.newRequestQueue(context).add(request)
    }
}