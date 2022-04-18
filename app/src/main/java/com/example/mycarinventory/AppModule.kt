package com.example.mycarinventory


import com.example.mycarinventory.service.IPartService
import com.example.mycarinventory.service.PartService
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel


val appModule = module {
    viewModel {MainViewModel(get())}
    single<IPartService>{PartService()}
}