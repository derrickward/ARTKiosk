package com.artsampleApp

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LocationHandler : LocationListener {

    private var networkLocation : Location? = null
    private var GPSLocation : Location? = null
    private var handler = Handler()

    fun acquireLocation(context: Context, completion: (location: Location?) -> Unit)
    {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, Looper.getMainLooper())
        }

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, Looper.getMainLooper())
            handler.postDelayed({
                try {
                    val networkLocationVal = networkLocation
                    val gpsLocationVal = GPSLocation
                    if (gpsLocationVal != null) {
                        completion(gpsLocationVal)
                    } else if (networkLocationVal != null) {
                        completion(networkLocationVal)
                    }
                    else
                    {
                        completion(null)
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                    completion(null)
                }
            }, 6000)
        }
        else
        {
            completion(null)
        }

    }

    override fun onLocationChanged(location: Location?) {
        if(location?.getProvider().equals(LocationManager.NETWORK_PROVIDER))
        {
            networkLocation = location;
        }
        else if(location?.getProvider().equals(LocationManager.GPS_PROVIDER))
        {
            GPSLocation = location;
        }

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }
}