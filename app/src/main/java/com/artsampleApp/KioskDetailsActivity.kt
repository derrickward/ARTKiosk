package com.artsampleApp

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.artsampleApp.databinding.ActivityKioskDetailsBinding
import com.artsampleApp.repository.KioskRepository
import com.squareup.picasso.Picasso

class KioskDetailsActivity : AppCompatActivity() {

    private val imageURL = "https://automatedrt.com/wp-content/uploads/2020/11/baked-machines.png"
    lateinit var viewModel : KioskDetailsViewModel
    lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val provider = ViewModelProvider(this)
        viewModel = provider.get(KioskDetailsViewModel::class.java)

        val binding : ActivityKioskDetailsBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_kiosk_details)
        binding.setLifecycleOwner(this)
        binding.item = viewModel
        binding.executePendingBindings()

        imageView = findViewById(R.id.imageView)
        val kioskIdx = intent.getIntExtra(KIOSK_IDX_EXTRA, 0)
        viewModel.kiosk = KioskRepository.kiosks[kioskIdx]
        viewModel.update()
    }

    override fun onStart() {
        super.onStart()
        Picasso.get().load(imageURL).into(imageView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object
    {
        const val KIOSK_IDX_EXTRA = "kiosk_idx"
    }
}