package com.matthewscorp.schwab.interview.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PizzaResponse(
    var authenticationResultCode: String,
    var brandLogoUri: String,
    var copyright: String,
    var resourceSets: List<PizzaSet>,
    var statusCode: Int,
    var statusDescription: String,
    var traceId: String
)

data class PizzaSet(
    var estimatedTotal: Int,
    var resources : List<PizzaPlaces>
)

@Parcelize
data class PizzaPlaces(
    var name: String,
    var Address: Address,
    var PhoneNumber: String,
    var Website: String,
    var entityType: String
) : Parcelable

@Parcelize
data class Address(
    var addressLine: String,
    var adminDistrict: String,
    var formattedAddress: String,
    var locality: String,
    var postalCode: String
): Parcelable