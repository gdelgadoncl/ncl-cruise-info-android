package com.ncl.nclcruiseinfo

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NCLCruiseInfoApi {
    @GET("cms-service/cruise-ships/{shipName}")
    suspend fun getCruiseShipInfo(@Path("shipName") shipName: String): Response<CruiseShipInfo>
}