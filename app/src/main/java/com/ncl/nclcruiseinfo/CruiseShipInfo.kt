package com.ncl.nclcruiseinfo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CruiseShipInfo(
    val shipName: String,
    val imagePath: List<String>,
    val code: String,
    val shipDescription: String,
    val whatsIncluded: List<String>,
    val shipFacts: ShipFacts
) : Parcelable

@Parcelize
data class ShipFacts(
    val passengerCapacity: String,
    val grossRegisterTonnage: String,
    val overallLength: String,
    val maxBeam: String,
    val draft: String,
    val engines: String,
    val cruiseSpeed: String,
    val crew: String,
    val inauguralDate: String,
    val remodeledDate: String?
) : Parcelable
