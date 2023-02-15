package com.julo.weatherdex.base.network.di.module

import android.content.Context
import androidx.viewbinding.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(
        @Named("baseUrl") baseUrl: String,
        gsonConverterFactory: Converter.Factory,
        okHttpClient: OkHttpClient,
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory() : Converter.Factory = GsonConverterFactory.create()

    @Provides
    fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
        val httpBuilder = OkHttpClient
            .Builder()

        if(BuildConfig.DEBUG) {
            httpBuilder.addInterceptor(chuckerInterceptor)
        }

        return httpBuilder.build()
    }

    @Provides
    fun provideChuckerInterceptor(
        context: Context
    ): ChuckerInterceptor = ChuckerInterceptor.Builder(context).build()
}
