package com.practicum.resp_toi_app.di

import android.content.Context
import com.practicum.resp_toi_app.data.api.NetworkClient
import com.practicum.resp_toi_app.data.impl.BossesRepositoryImpl
import com.practicum.resp_toi_app.data.network.RespToiApi
import com.practicum.resp_toi_app.data.network.RetrofitNetworkClient
import com.practicum.resp_toi_app.domain.api.BossesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideNetworkClient(@ApplicationContext context: Context, respToiApi: RespToiApi): NetworkClient {
        return RetrofitNetworkClient(context, respToiApi)
    }

    @Provides
    @Singleton
    fun provideRespToiApi(): RespToiApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://resp-toi.ru")
            .build()
            .create(RespToiApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBossesRepository(networkClient: NetworkClient): BossesRepository {
        return BossesRepositoryImpl(networkClient)
    }
}