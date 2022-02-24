package com.example.graphqlandcomposepoc.model

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.Logger
import com.apollographql.apollo.coroutines.await
import com.example.graphqlandcomposepoc.LaunchesPastQuery

/**
 * Experts: Daniel_Zombori
 * Created: 22/02/2022
 */
object MainRepository {
    private val apolloClient by lazy {  ApolloClient.builder()
        .serverUrl("https://api.spacex.land/graphql/")
        .logger(object : Logger {
            override fun log(priority: Int, message: String, t: Throwable?, vararg args: Any) {
                Log.d("APOLLO", message)
            }

        })
        .build()
    }

    suspend fun getPastLaunchesData(): LaunchesPastQuery.Data? {
        return apolloClient.query(LaunchesPastQuery()).await().data
    }

//    fun getPastLaunchesData() {
//        val executorService: ExecutorService = Executors.newFixedThreadPool(4)
//        executorService.execute {
//            apolloClient.query(LaunchesPastQuery()).watcher().enqueueAndWatch(object: ApolloCall.Callback<LaunchesPastQuery.Data>() {
//                override fun onFailure(e: ApolloException) {
//                    Log.e("APOLLO", e.message ?: "")
//                }
//
//                override fun onResponse(response: Response<LaunchesPastQuery.Data>) {
//                    response.data
//                }
//            })
//        }
//    }
}