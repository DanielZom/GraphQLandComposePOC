package com.example.graphqlandcomposepoc

import com.example.graphqlandcomposepoc.model.IMainRepository
import com.example.graphqlandcomposepoc.model.MainRepository
import com.example.graphqlandcomposepoc.view.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Experts: Daniel_Zombori
 * Created: 28/02/2022
 */
object Koin {
    val module = module {
        single<IMainRepository> { MainRepository() }
        viewModel { MainViewModel(get()) }
    }
}