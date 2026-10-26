package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.model.response.ModelDocList
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.request.SwitchAccountRequest
import com.bytheour.data.response.CommonResponse
import com.bytheour.data.response.GetNotificationStatusResponse
import com.bytheour.data.response.LoginNewResponse
import com.bytheour.data.response.ModelDocUrl
import com.bytheour.data.response.ModelInviteCode
import com.bytheour.data.response.ModelNotificationList
import com.bytheour.data.response.ModelSwitchStatus
import com.bytheour.data.response.StripeManageAccountModel
import com.bytheour.repository.CommonRepository
import com.bytheour.showProgress

class CommonUseCases(private val commonRepository: CommonRepository) {

    suspend fun updateNotificationsStatus(allowPush: String): JtApiResult<LoginNewResponse> {
        showProgress.value = true
        val result = commonRepository.updateNotificationStatus(allowPush)
        showProgress.value = false
        return result
    }

    suspend fun updateVisibility(avilability: String): JtApiResult<GetNotificationStatusResponse> {
        showProgress.value = true
        val result = commonRepository.updateVisibility(avilability)
        showProgress.value = false
        return result
    }

    suspend fun getProfile(): JtApiResult<LoginNewResponse> {
        showProgress.value = true
        val result = commonRepository.getProfile()
        showProgress.value = false
        return result
    }

    suspend fun updateProfile(
        firstName: String?,
        lastName: String?,
        email: String?,
        country: String?,
        number: String?,
        profile: String?,
        about: String?,
        userName :String?
    ): JtApiResult<LoginNewResponse> {
        showProgress.value = true
        val result = commonRepository.updateProfile(
            firstName,
            lastName,
            email,
            country,
            number,
            profile,
            about,
            userName
        )
        showProgress.value = false
        return result
    }

    suspend fun sendReport(
        subject: String,
        description: String,
    ): JtApiResult<CommonResponse> {
        showProgress.value = true
        val result = commonRepository.sendReportAnIssue(
            subject = subject,
            description = description
        )
        showProgress.value = false
        return result
    }

    suspend fun gerProfileUrl(
        contentType: String,
        extension: String,
    ): JtApiResult<ModelDocUrl> {
        showProgress.value = true
        val result = commonRepository.getProfile(
            contentType = contentType,
            extension = extension,
        )
        showProgress.value = false
        return result
    }


    suspend fun getGalleryImageUrl(
        contentType: String,
        extension: String,
    ): JtApiResult<ModelDocUrl> {
        showProgress.value = true
        val result = commonRepository.getGalleryImageUrl(
            contentType = contentType,
            extension = extension,
        )
        showProgress.value = false
        return result
    }



    suspend fun uploadAwsDocSafe(
        uploadUrl: String,
        fileBytes: ByteArray,
    ): JtApiResult<Unit> {
        showProgress.value = true
        val result = commonRepository.uploadAwsDocSafe(
            uploadUrl = uploadUrl,
            fileBytes = fileBytes
        )
        showProgress.value = false
        return result
    }

    suspend fun Delete(
        reason: String,
    ): JtApiResult<CommonResponse> {
        showProgress.value = true
        val result = commonRepository.deleteAccount(
            reason = reason
        )
        showProgress.value = false
        return result
    }

    suspend fun logOut():JtApiResult<CommonResponse>{
        showProgress.value = true
        val result = commonRepository.logOut()
        showProgress.value = false
        return result
    }

    suspend fun getRating(
        limit: String,
        offset: String,
        ratingType: String?,
        ratings: String?,
        serviceRating: String?
    ): JtApiResult<ModelGetRating> {
        showProgress.value = true
      val result = commonRepository.getRating(
            limit = limit,
            offset = offset,
            ratingType = ratingType,
            ratings = ratings,
            serviceRating = serviceRating
        )
        showProgress.value = false
        return result
    }

    suspend fun getNotificationList(
        limit: String,
        offset: String
    ): JtApiResult<ModelNotificationList> {
        showProgress.value = true
        val result = commonRepository.getNotificationList(
            limit = limit,
            offset = offset
        )
        showProgress.value = false
        return result
    }


    suspend fun switchAccount(
        payload: SwitchAccountRequest
    ):JtApiResult<LoginNewResponse> {
        showProgress.value = true
        val result = commonRepository.switchAccount(payload)
        showProgress.value = false
        return result
    }


    suspend fun switchAccountStatus(): JtApiResult<ModelSwitchStatus> {
        showProgress.value = true
        val result = commonRepository.switchAccountStatus()
        showProgress.value = false
        return result
    }



    suspend fun getDocument(): JtApiResult<ModelDocList> {
        showProgress.value = true
        val result = commonRepository.getDocuments()
        showProgress.value = false
        return result
    }


    suspend fun generateChatAttachmentUrl(
        contentType: String,
        extension: String,
    ): JtApiResult<ModelDocUrl> {
        showProgress.value = true
        val result = commonRepository.generateChatAttachmentUrl(
            contentType = contentType,
            extension = extension,
        )
        showProgress.value = false
        return result
    }

    suspend fun verifyAccountWithStripe(): JtApiResult<StripeManageAccountModel> {
        showProgress.value = true
        val result = commonRepository.verifyAccountWithStripe()
        showProgress.value = false
        return result
    }
    suspend fun getInviteCode(): JtApiResult<ModelInviteCode> {
        showProgress.value = true
        val result = commonRepository.getInviteCode()
        showProgress.value = false
        return result
    }


}
