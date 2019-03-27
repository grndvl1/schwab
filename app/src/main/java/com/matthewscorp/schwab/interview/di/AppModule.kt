package com.matthewscorp.schwab.interview.di

import com.matthewscorp.schwab.interview.repos.PizzaRepositories
import com.matthewscorp.schwab.interview.viewmodels.PizzaViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PizzaRepositories() }
    viewModel { PizzaViewModel(get()) }
}

