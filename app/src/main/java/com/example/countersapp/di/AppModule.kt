package com.example.countersapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.countersapp.data.api.CountersService
import com.example.countersapp.data.api.Endpoints.URL_BASE
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(URL_BASE)
            .build()
    }

    @Singleton
    @Provides
    fun provideCountersService(retrofit: Retrofit): CountersService {
        return retrofit.create(CountersService::class.java)
    }

    @Singleton
    @Provides
    fun providePreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("counters", 0)

}