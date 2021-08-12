package com.ncl.nclcruiseinfo

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        MainActivityViewModel(get())
    }
}