package com.artsampleApp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artsampleApp.databinding.ActivityKioskListBinding
import com.artsampleApp.repository.KioskRepository

class KioskListActivity : AppCompatActivity(), KioskItemListener {

    lateinit var viewModel : KioskListViewModel
    lateinit var kioskRecyclerView : RecyclerView
    lateinit var adapter : KioskItemAdapter
    private var isLoaded = false
    val locationHandler = LocationHandler()

    private val DEFAULT_LAT = 29.82713679999999
    private val DEFAULT_LONG = -94.8491468
    private val REQUEST_CODE_ASK_PERMISSION_LOCATION = 456

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val provider = ViewModelProvider(this)
        viewModel = provider.get(KioskListViewModel::class.java)

        val binding : ActivityKioskListBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_kiosk_list)
        binding.setLifecycleOwner(this)
        binding.item = viewModel
        binding.executePendingBindings()

        kioskRecyclerView = findViewById(R.id.kioskRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        kioskRecyclerView.layoutManager = layoutManager
        adapter = KioskItemAdapter(KioskRepository.kiosks, this)
        kioskRecyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        if(!isLoaded) {
            isLoaded = true

            if (askForLocationPermission()) {
                loadKiosksByLocation()
            }
            else
            {
                loadWithDefaultLocation()
            }
        }
    }

    fun loadKiosksByLocation()
    {
        viewModel.progressVisibility.value = View.VISIBLE
        viewModel.loadingText.value = "Acquiring Location..."
        viewModel.isEnabled.value = false

        locationHandler.acquireLocation(this)
        { location ->
            runOnUiThread { viewModel.loadingText.value = getString(R.string.loading_kiosks) }

            if(location != null) {
                viewModel.load(this, location.latitude, location.longitude)
                {

                    onLoaded()
                }
            }
            else
            {
                onLoaded()
            }
        }

    }

    fun loadWithDefaultLocation()
    {
        runOnUiThread { viewModel.loadingText.value = getString(R.string.loading_kiosks)  }
        viewModel.load(this, DEFAULT_LAT, DEFAULT_LONG)
        {
            onLoaded()
        }
    }

    fun onLoaded()
    {
        runOnUiThread {
            viewModel.progressVisibility.value = View.GONE
            viewModel.isEnabled.value = true
            adapter.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            when (requestCode) {
                REQUEST_CODE_ASK_PERMISSION_LOCATION -> {
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        onLocationPermissionGranted()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun onLocationPermissionGranted()
    {
        loadKiosksByLocation()
    }

    override fun onSelected(item: Int) {
        startActivity(Intent(this, KioskDetailsActivity::class.java)
                .putExtra(KioskDetailsActivity.KIOSK_IDX_EXTRA,item))
    }

    protected fun askForLocationPermission(): Boolean {
        val isEnabled: Boolean =
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val permissions: Array<String> = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        if (!isEnabled) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showLocationServicesPopup()
            } else {
                requestPermissions(permissions,
                        REQUEST_CODE_ASK_PERMISSION_LOCATION)
            }
        }
        return isEnabled
    }

    private fun showLocationServicesPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage("Location services must be enabled in order to show nearby kiosks. Would you like to enable them?")
        builder.setCancelable(true)
        builder.setPositiveButton("yes") { dialog, id ->
            this@KioskListActivity.requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_ASK_PERMISSION_LOCATION)
        }
        builder.setNegativeButton("no") { dialog, id ->  }
        builder.create().show()
    }
}