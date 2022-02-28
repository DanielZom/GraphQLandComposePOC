package com.example.graphqlandcomposepoc.model

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.example.graphqlandcomposepoc.LaunchesPastQuery

/**
 * Experts: Daniel_Zombori
 * Created: 22/02/2022
 */
interface IMainRepository {
    suspend fun getPastLaunchesData(limit: Int): LaunchesPastQuery.Data?
}

class MainRepository: IMainRepository {
    private val BASE_URL = "https://api.spacex.land/graphql/"
    private val apolloClient by lazy {  ApolloClient.builder()
        .serverUrl(BASE_URL)
        .build()
    }

    override suspend fun getPastLaunchesData(limit: Int): LaunchesPastQuery.Data? {
        return apolloClient.query(LaunchesPastQuery(limit)).await().data
    }
}