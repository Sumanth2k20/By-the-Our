package com.bytheour.core

import com.bytheour.repository.AdminRepository
import com.bytheour.repository.ChatRepository
import com.bytheour.repository.CommonBookingRepository
import com.bytheour.repository.CommonChatSupportTicketRepository
import com.bytheour.repository.CommonPaymentRepository
import com.bytheour.repository.UserBookingsRepository
import com.bytheour.repository.CommonRepository
import com.bytheour.repository.CommonTransactionRepository
import com.bytheour.repository.EntryModuleRepository
import com.bytheour.repository.GalleryRepository
import com.bytheour.repository.GeneralRepository
import com.bytheour.repository.ProviderBookingRepository
import com.bytheour.repository.ProvidersRepository
import com.bytheour.repository.ProviderServicesRepository
import com.bytheour.repository.StripeRepository
import com.bytheour.repository.UserRepository
import com.bytheour.useCases.AdminUseCases
import com.bytheour.useCases.ChatUseCases
import com.bytheour.useCases.CommonBookingUseCase
import com.bytheour.useCases.CommonChatSupportTicketUseCases
import com.bytheour.useCases.CommonPaymentUseCase
import com.bytheour.useCases.CommonTransactionUseCase
import com.bytheour.useCases.UserBookingsUseCases
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.EntryModuleUseCases
import com.bytheour.useCases.GalleryUseCases
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.useCases.ProviderServicesUseCases
import com.bytheour.useCases.ProvidersUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.useCases.UserUseCases
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }
    }

    single { Settings()}
    single { SharedPreferenceUseCase(get())}
    single { GeneralUseCases(get()) }
    single { GeneralRepository(get()) }
    single { CommonRepository(get()) }
    single { CommonUseCases(get()) }
    single { AdminRepository(get()) }
    single { AdminUseCases(get()) }
    single { ChatRepository(get()) }
    single { ChatUseCases(get()) }
    single { EntryModuleUseCases<Any?>(get()) }
    single { EntryModuleRepository(get()) }
    single { UserBookingsRepository(get()) }
    single { UserBookingsUseCases(get()) }
    single { CommonBookingRepository(get()) }
    single { CommonBookingUseCase(get()) }
    single { ProviderBookingRepository(get()) }
    single { ProviderBookingUseCases(get()) }
    single { UserRepository(get()) }
    single { UserUseCases(get()) }
    single { ProvidersRepository(get()) }
    single { ProvidersUseCases(get()) }
    single { ProviderServicesRepository(get()) }
    single { ProviderServicesUseCases(get()) }
    single { CommonPaymentUseCase(get(),get()) }
    single { CommonTransactionUseCase(get()) }
    single { CommonTransactionRepository(get()) }
    single { CommonPaymentRepository(get()) }
    single { CommonChatSupportTicketUseCases(get()) }
    single { CommonChatSupportTicketRepository(get()) }
    single { GalleryRepository(get()) }
    single { GalleryUseCases(get()) }
    single { StripeRepository(get()) }



}

