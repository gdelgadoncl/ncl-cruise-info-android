package com.ncl.nclcruiseinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel(private val nclCruiseInfoApi: NCLCruiseInfoApi) : ViewModel() {

    private val shipNames = listOf("GEM", "SKY", "BLISS", "ENCORE")
    private val _cruiseShips = MutableLiveData<List<CruiseShipInfo>>()
    val cruiseShips: LiveData<List<CruiseShipInfo>>
        get() = _cruiseShips

    fun loadData() {
        viewModelScope.launch {
            val cruiseInfoList = mutableListOf<CruiseShipInfo>()
            shipNames.forEach {
                val response = nclCruiseInfoApi.getCruiseShipInfo(it)
                response.body()?.let { info ->
                    cruiseInfoList.add(info)
                }
            }
            _cruiseShips.value = cruiseInfoList
        }
    }
}