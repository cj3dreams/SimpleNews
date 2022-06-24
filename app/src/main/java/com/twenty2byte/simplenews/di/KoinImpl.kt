package com.twenty2byte.simplenews.di

import android.app.Application
import androidx.room.Room
import com.twenty2byte.simplenews.BuildConfig
import com.twenty2byte.simplenews.source.local.RoomAppDb
import com.twenty2byte.simplenews.source.remote.RestApiRequests
import com.twenty2byte.simplenews.vm.NewsViewModel
import com.twenty2byte.simplenews.vm.RoomViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://newsapi.org/v2/"

val userDb = module {

    fun provideDataBase(application: Application) =
        Room.databaseBuilder(application, RoomAppDb::class.java, "AppDb")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    fun provideDao(database: RoomAppDb) = database.newsDao()

    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
}

val networkModule = module {

    fun <Api> provideRemoteDataSource(api: Class<Api>): Api =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)

    factory { provideRemoteDataSource(RestApiRequests::class.java) }

    viewModel {
        NewsViewModel(get())
    }
}

val viewModel = module {
    viewModel {
        RoomViewModel(get())
    }
}