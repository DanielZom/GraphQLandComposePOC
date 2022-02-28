package com.example.graphqlandcomposepoc.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.graphqlandcomposepoc.LaunchesPastQuery
import com.example.graphqlandcomposepoc.model.IMainRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Experts: Daniel_Zombori
 * Created: 22/02/2022
 */
interface IMainViewModel {
    fun getPastLaunchesData()
    val launchJsonAdapter: JsonAdapter<LaunchesPastQuery.LaunchesPast>
    val state: MutableState<List<LaunchesPastQuery.LaunchesPast?>?>
}

class MainViewModel(private val mainRepository: IMainRepository) : ViewModel(), IMainViewModel {
    override val state: MutableState<List<LaunchesPastQuery.LaunchesPast?>?> by lazy {
        mutableStateOf(
            null
        )
    }
    override val launchJsonAdapter: JsonAdapter<LaunchesPastQuery.LaunchesPast> by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            .adapter(LaunchesPastQuery.LaunchesPast::class.java)
    }

    override fun getPastLaunchesData() {
        viewModelScope.launch {
            state.value = mainRepository.getPastLaunchesData(20)?.launchesPast
        }
    }
}

fun LaunchesPastQuery.LaunchesPast.getFormattedLaunchDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return if (launch_date_local != null) {
        val launchDate = dateFormat.parse(launch_date_local.toString()) ?: ""
        return dateFormat.format(launchDate)
    } else {
        ""
    }
}