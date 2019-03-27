package com.matthewscorp.schwab.interview.data

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
    var resources : List<PizzaList>
)

data class PizzaList(
    var name: String,
    var Address: Address,
    var PhoneNumber: String,
    var Website: String,
    var entityType: String
)

data class Address(
    var addressLine: String,
    var adminDistrict: String,
    var formattedAddress: String,
    var locality: String,
    var postalCode: String
)