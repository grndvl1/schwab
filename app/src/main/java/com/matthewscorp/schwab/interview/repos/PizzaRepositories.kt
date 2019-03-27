package com.matthewscorp.schwab.interview.repos

import android.arch.lifecycle.MutableLiveData
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import com.matthewscorp.schwab.interview.data.PizzaResponse
import com.matthewscorp.schwab.interview.data.PizzaSet
import com.matthewscorp.schwab.interview.data.Resource
import com.matthewscorp.schwab.interview.repos.api.getPizzaCollection

class PizzaRepositories {

    var data: MutableLiveData<Resource<PizzaSet>> = MutableLiveData()

    fun getPizzaData() : MutableLiveData<Resource<PizzaSet>> {
        return data
    }

    fun setQueryAndLocation(query: String, location: String) {
        data.value = Resource.loading()
        getPizzaCollection(query, location). setPriority(Priority.LOW).build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    response?.let {
                        var repo = Gson().fromJson(it, PizzaResponse::class.java)
                        repo?.resourceSets?.let{ listItems->
                            if (listItems.isNotEmpty() && listItems[0].estimatedTotal > 0) {
                                data.value = Resource.success(listItems[0]) // only want the first set
                            } else {
                                data.value = Resource.error("Zero data items")
                            }
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    data.value = Resource.error("$query search error")
                }
            })
    }
}