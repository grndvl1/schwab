package com.matthewscorp.schwab.interview.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.matthewscorp.schwab.interview.data.PizzaSet
import com.matthewscorp.schwab.interview.data.Resource
import com.matthewscorp.schwab.interview.repos.PizzaRepositories

class PizzaViewModel(val repo: PizzaRepositories) : ViewModel() {
    fun init(query: String, location : String) {
        repo.setQueryAndLocation(query, location)
    }

    fun getPizzaData(): LiveData<Resource<PizzaSet>> {
        return repo.getPizzaData()
    }
}