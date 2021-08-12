package com.ncl.nclcruiseinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncl.nclcruiseinfo.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel.loadData()

        viewModel.cruiseShips.observe(this) {
            val adapter = CruiseInfoListAdapter(it) { cruiseInfo ->
                CruiseInfoDetailsActivity.startActivity(this, cruiseInfo)
            }
            binding.cruiseRecyclerView.adapter = adapter
            binding.cruiseRecyclerView.layoutManager = LinearLayoutManager(this)
            adapter.notifyDataSetChanged()
        }
    }
}