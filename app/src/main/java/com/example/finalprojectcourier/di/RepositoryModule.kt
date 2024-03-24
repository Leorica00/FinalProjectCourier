package com.example.finalprojectcourier.di

import com.example.final_project.data.remote.common.EmailSignInResponseHandler
import com.example.final_project.data.remote.common.ResponseHandler
import com.example.final_project.data.remote.service.ChatBotApiService
import com.example.finalprojectcourier.data.remote.service.DirectionsApiService
import com.example.final_project.data.repository.remote.firebase.FirebaseAdditionalUserDataRepositoryImpl
import com.example.final_project.data.repository.remote.firebase.FirebasePhonePhoneAuthRepositoryImpl
import com.example.final_project.data.repository.remote.chat.ChatContactsRepositoryImpl
import com.example.final_project.data.repository.remote.chat.ChatMessagesRepositoryImpl
import com.example.final_project.data.repository.remote.firebase.FirebaseAuthStateRepositoryImpl
import com.example.final_project.data.repository.remote.firebase.FirebaseEmailLoginRepositoryImpl
import com.example.final_project.data.repository.remote.firebase.FirebasePhotosRepositoryImpl
import com.example.final_project.data.repository.remote.firebase.FirebaseSignOutRepositoryImpl
import com.example.final_project.data.repository.remote.firebase.FirebaseUserDataRepositoryImpl
import com.example.finalprojectcourier.data.repository.remote.route.DirectionsRepositoryImpl
import com.example.final_project.di.DispatchersModule.IoDispatcher
import com.example.final_project.domain.repository.auth.FirebaseAdditionalUserDataRepository
import com.example.final_project.domain.repository.auth.FirebaseAuthStateRepository
import com.example.final_project.domain.repository.auth.FirebaseEmailLoginRepository
import com.example.final_project.domain.repository.auth.FirebasePhoneAuthRepository
import com.example.final_project.domain.repository.auth.FirebaseSignOutRepository
import com.example.final_project.domain.repository.bot.ChatBotRepository
import com.example.final_project.domain.repository.chat.ChatContactsRepository
import com.example.final_project.domain.repository.chat.ChatMessagesRepository
import com.example.final_project.domain.repository.firebase.FirebasePhotosRepository
import com.example.final_project.domain.repository.firebase.FirebaseUserDataRepository
import com.example.final_project.domain.repository.route.DirectionsRepository
import com.example.finalprojectcourier.data.local.datasource.ChatBotAuthTokenDataSource
import com.example.finalprojectcourier.data.remote.service.GoogleDistanceMatrixApiService
import com.example.finalprojectcourier.data.repository.remote.chatbot.ChatBotRepositoryImpl
import com.example.finalprojectcourier.data.repository.remote.distance.DistanceRepositoryImpl
import com.example.finalprojectcourier.data.repository.remote.location.LocationDeliveryRepositoryImpl
import com.example.finalprojectcourier.data.repository.remote.order.OrderRepositoryImpl
import com.example.finalprojectcourier.domain.repository.distance.DistanceRepository
import com.example.finalprojectcourier.domain.repository.location.LocationDeliveryRepository
import com.example.finalprojectcourier.domain.repository.order.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideOrderRepository(databaseReference: DatabaseReference): OrderRepository = OrderRepositoryImpl(databaseReference)

    @Provides
    @Singleton
    fun provideDirectionsRepository(directionsApiService: DirectionsApiService, @IoDispatcher ioDispatcher: CoroutineDispatcher): DirectionsRepository =
        DirectionsRepositoryImpl(directionsApiService, ioDispatcher )

    @Provides
    @Singleton
    fun provideDistanceRepository(distanceMatrixApiService: GoogleDistanceMatrixApiService, @IoDispatcher ioDispatcher: CoroutineDispatcher): DistanceRepository =
        DistanceRepositoryImpl(distanceMatrixApiService, ioDispatcher)

    @Provides
    @Singleton
    fun provideLocationDeliveryRepository(databaseReference: DatabaseReference, @IoDispatcher ioDispatcher: CoroutineDispatcher): LocationDeliveryRepository =
        LocationDeliveryRepositoryImpl(databaseReference, ioDispatcher)

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(auth: FirebaseAuth, @IoDispatcher ioDispatcher: CoroutineDispatcher) : FirebasePhoneAuthRepository {
        return FirebasePhonePhoneAuthRepositoryImpl(auth = auth, ioDispatcher = ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideChatBotRepository(responseHandler: ResponseHandler, chatBotApiService: ChatBotApiService, chatBotAuthTokenDataSource: ChatBotAuthTokenDataSource) : ChatBotRepository {
        return ChatBotRepositoryImpl(handler = responseHandler, chatBotApiService = chatBotApiService, chatBotAuthTokenDataSource = chatBotAuthTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideFirebaseAdditionalUserDataRepository(auth: FirebaseAuth, @IoDispatcher ioDispatcher: CoroutineDispatcher) : FirebaseAdditionalUserDataRepository {
        return FirebaseAdditionalUserDataRepositoryImpl(auth = auth, ioDispatcher = ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideFirebaseSignOutRepository(auth: FirebaseAuth, @IoDispatcher ioDispatcher: CoroutineDispatcher) : FirebaseSignOutRepository {
        return FirebaseSignOutRepositoryImpl(auth = auth, ioDispatcher = ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideEmailSignInRepository(auth: FirebaseAuth, emailSignInResponseHandler: EmailSignInResponseHandler) : FirebaseEmailLoginRepository {
        return FirebaseEmailLoginRepositoryImpl(auth = auth, emailSignInResponseHandler = emailSignInResponseHandler)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthStateRepository(auth: FirebaseAuth, @IoDispatcher ioDispatcher: CoroutineDispatcher) : FirebaseAuthStateRepository {
        return FirebaseAuthStateRepositoryImpl(auth = auth, ioDispatcher = ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideFirebaseUserDataRepository(auth: FirebaseAuth, @IoDispatcher ioDispatcher: CoroutineDispatcher) : FirebaseUserDataRepository {
        return FirebaseUserDataRepositoryImpl(auth = auth, ioDispatcher = ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideFirebasePhotosRepository(auth: FirebaseAuth, firebaseStorage: FirebaseStorage) : FirebasePhotosRepository {
        return FirebasePhotosRepositoryImpl(auth = auth, firebaseStorage = firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideChatRepository(auth: FirebaseAuth, databaseReference: DatabaseReference): ChatMessagesRepository = ChatMessagesRepositoryImpl(auth = auth, databaseReference = databaseReference)

    @Provides
    @Singleton
    fun provideChatContactsRepository(auth: FirebaseAuth ,databaseReference: DatabaseReference): ChatContactsRepository = ChatContactsRepositoryImpl(auth = auth, databaseReference = databaseReference)
}