package com.moonlitdoor.release.it

import android.preference.PreferenceManager
import androidx.room.Room
import com.moonlitdoor.release.it.auth.AuthViewModel
import com.moonlitdoor.release.it.domain.api.GithubApi
import com.moonlitdoor.release.it.domain.dao.AppDatabase
import com.moonlitdoor.release.it.domain.dao.AuthDao
import com.moonlitdoor.release.it.domain.dao.Migrations
import com.moonlitdoor.release.it.domain.repository.AuthRepository
import com.moonlitdoor.release.it.domain.repository.ReleaseRepository
import com.moonlitdoor.release.it.domain.repository.RepoRepository
import com.moonlitdoor.release.it.repository.RepositoryViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val HOST = "HOST"

private const val AUTH_HOST = "https://github.com/"
private const val BASE_URL = "https://api.github.com/"
private const val ACCEPT_HEADER = "Accept"
private const val ACCEPT_VALUE = "application/vnd.github.v3+json"
private const val USER_AGENT_HEADER = "UserGraph-Agent"
private const val USER_AGENT_VALUE = "moonlitdoor/release-it"
private const val AUTHORIZATION_VALUE = "token %s"
private const val AUTHORIZATION_HEADER = "Authorization"

val di = module {

  single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME).addMigrations(*Migrations.ALL).build() }
  single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
  single { AuthDao(get()) }
  single(name = HOST) { AUTH_HOST }
  single {
    OkHttpClient.Builder()
      .addInterceptor(
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
      )
      .addNetworkInterceptor { chain ->
        chain.proceed(
          chain.request().newBuilder()
            .addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_VALUE.format(get<AuthDao>().getAuthToken()))
            .addHeader(ACCEPT_HEADER, ACCEPT_VALUE)
            .addHeader(USER_AGENT_HEADER, USER_AGENT_VALUE)
            .build()
        )
      }
      .build()
  }
  single {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .client(get())
      .build()
  }
  single { get<Retrofit>().create(GithubApi::class.java) }
  single { get<AppDatabase>().repoDao() }
  single { get<AppDatabase>().userDao() }
  single { get<AppDatabase>().releaseDao() }
  single { AuthRepository(get()) }
  single { RepoRepository(get()) }
  single { ReleaseRepository(get()) }
  viewModel { AuthViewModel(get()) }
  viewModel { RepositoryViewModel(androidApplication(), get(), get(), get()) }
}