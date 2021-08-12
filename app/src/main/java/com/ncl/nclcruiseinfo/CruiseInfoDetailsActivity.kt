package com.ncl.nclcruiseinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ncl.nclcruiseinfo.databinding.ActivityCruiseInfoDetailsBinding
import com.ncl.nclcruiseinfo.databinding.ActivityMainBinding
import java.util.*

class CruiseInfoDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCruiseInfoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCruiseInfoDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        intent.extras?.getParcelable<CruiseShipInfo>(EXTRA_CRUISE_SHIP_INFO)?.let {
            Glide.with(this)
                .load("http://ncl.com/" + it.imagePath.first().split("||").first())
                .centerCrop()
                .into(binding.cruiseDetailImageView)


            val adapter = ShipFactsListAdapter(it)
            binding.shipFactsRecyclerView.adapter = adapter
            binding.shipFactsRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    companion object {

        const val EXTRA_CRUISE_SHIP_INFO = "extra_cruise_info"

        fun startActivity(a: Activity, cruiseInfo: CruiseShipInfo) {
            a.startActivity(Intent(a, CruiseInfoDetailsActivity::class.java).apply {
                putExtra(EXTRA_CRUISE_SHIP_INFO, cruiseInfo)
            })
        }
    }
}