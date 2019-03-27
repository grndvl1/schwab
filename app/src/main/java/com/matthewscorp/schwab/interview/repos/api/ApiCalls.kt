package com.matthewscorp.schwab.interview.repos.api

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest

// query would be something like "pizza", location is gps coords in the form {latitude,longitude} i.e. 41.6271023,-88.2355845
fun getPizzaCollection(query: String, location: String) : ANRequest.GetRequestBuilder<out ANRequest.GetRequestBuilder<*>> {
    return AndroidNetworking.get("https://dev.virtualearth.net/REST/v1/LocalSearch/?query=$query&userLocation=$location&key=As5JEnBjWPLqma978M9rm2CJuh6S6oiAVaL030IshwWvL-JuGhy8nxnYvfakifKU")
}