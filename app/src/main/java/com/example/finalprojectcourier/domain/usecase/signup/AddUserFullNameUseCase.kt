package com.example.finalprojectcourier.domain.usecase.signup

import com.example.final_project.data.remote.common.Resource
import com.example.final_project.domain.model.GetUserAdditionalData
import com.example.final_project.domain.repository.auth.FirebaseAdditionalUserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddUserFullNameUseCase @Inject constructor(private val additionalUserDataRepository: FirebaseAdditionalUserDataRepository) {
    suspend operator fun invoke(data: GetUserAdditionalData) : Flow<Resource<Boolean>> {
        return additionalUserDataRepository.addUserFullName(data)
    }
}