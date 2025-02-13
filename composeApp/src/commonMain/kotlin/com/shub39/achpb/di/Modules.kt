package com.shub39.achpb.di

import com.shub39.achpb.browser.presentation.BrowserVM
import com.shub39.achpb.core.data.DatastoreFactory
import com.shub39.achpb.core.data.DataStoreImpl
import com.shub39.achpb.core.data.HttpClientFactory
import com.shub39.achpb.browser.domain.AnimeBoysRepo
import com.shub39.achpb.browser.domain.AnimeGirlsRepo
import com.shub39.achpb.browser.data.repository.AnimeBoysRepoImpl
import com.shub39.achpb.browser.data.repository.AnimeGirlsRepoImpl
import com.shub39.achpb.core.domain.AppDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // Network
    single { HttpClientFactory.create(get()) }
    singleOf(::AnimeBoysRepoImpl).bind<AnimeBoysRepo>()
    singleOf(::AnimeGirlsRepoImpl).bind<AnimeGirlsRepo>()

    // DataStore
    single(named("DataStore")) { get<DatastoreFactory>().getPreferencesDataStore() }
    single<AppDataStore> { DataStoreImpl(get(named("DataStore"))) }

    // ViewModel
    viewModelOf(::BrowserVM)
}