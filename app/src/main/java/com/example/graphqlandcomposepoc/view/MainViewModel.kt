package com.example.graphqlandcomposepoc.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graphqlandcomposepoc.LaunchesPastQuery
import com.example.graphqlandcomposepoc.model.MainRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

/**
 * Experts: Daniel_Zombori
 * Created: 22/02/2022
 */
class MainViewModel : ViewModel() {
    val state: MutableState<List<LaunchesPastQuery.LaunchesPast?>?> by lazy { mutableStateOf(null) }
    val launchJsonAdapter by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            .adapter(LaunchesPastQuery.LaunchesPast::class.java)
    }

    fun getPastLaunchesData() {
        viewModelScope.launch {
            state.value = MainRepository.getPastLaunchesData()?.launchesPast
        }
//        MainRepository.getPastLaunchesData()
    }
}

fun LaunchesPastQuery.LaunchesPast.getFormattedLaunchDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-HH:mm:ss")
    return "2020-11-12"
}