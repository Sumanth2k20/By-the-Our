package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.FaqRequest
import com.bytheour.data.request.GetServicesRequest
import com.bytheour.data.request.SearchServiceRequest
import com.bytheour.data.response.AppVersionResponse
import com.bytheour.data.response.CancelReasonsResponse
import com.bytheour.data.response.GetAppStatusResponse
import com.bytheour.data.response.GetTrendingServicesResponse
import com.bytheour.data.response.MediationGuidelineResponse
import com.bytheour.data.response.ModelCommon
import com.bytheour.data.response.ModelExpertVideo
import com.bytheour.data.response.ModelFaqResponse
import com.bytheour.data.response.PlaceDetailsResponse
import com.bytheour.data.response.Prediction
import com.bytheour.data.response.PrivacyAndPolicyResponse
import com.bytheour.repository.GeneralRepository
import com.bytheour.showProgress

class GeneralUseCases(private val generalRepository: GeneralRepository) {

    suspend fun getMediationGuideline(): JtApiResult<MediationGuidelineResponse?> {
        showProgress.value = true
        val result = generalRepository.getMediationGuideline()
        showProgress.value = false
        return result
    }

    suspend fun getPlacePredictions(query: String, apiKey: String): List<Prediction> {
        showProgress.value = true
        val result= generalRepository.getPlacePredictions(query, apiKey)
        showProgress.value = false
        return result
    }

    suspend fun getPlaceDetails(placeId: String, apiKey: String): PlaceDetailsResponse? {
        return generalRepository.getPlaceDetails(placeId, apiKey)
    }

    suspend fun getPrivacyAndPolicy(): JtApiResult<PrivacyAndPolicyResponse?> {
        showProgress.value = true
        val result = generalRepository.getPrivacyAndPolicy()
        showProgress.value = false
        return result
    }

    suspend fun appStatusUseCase(): JtApiResult<GetAppStatusResponse?> {
        val result = generalRepository.appStatus()
        return result
    }

    suspend fun getFaqUseCase(request: FaqRequest): JtApiResult<ModelFaqResponse?> {
        showProgress.value = true
        val result = generalRepository.getFaq(request)
        showProgress.value = false
        return result
    }

    suspend fun getAppVersion(): JtApiResult<AppVersionResponse?> {
        val result = generalRepository.getAppVersion()
        return result
    }

    suspend fun getServices(request: GetServicesRequest): JtApiResult<GetTrendingServicesResponse?> {
        showProgress.value = true
        val result = generalRepository.getServices(request)
        showProgress.value = false
        return result
    }

    suspend fun getCancelReason(): JtApiResult<CancelReasonsResponse?> {
        showProgress.value = true
        val result = generalRepository.cancelReasons()
        showProgress.value = false
        return result
    }

    suspend fun searchService(request : SearchServiceRequest): JtApiResult<GetTrendingServicesResponse> {
        showProgress.value = true
        val result = generalRepository.searchService(request)
        showProgress.value = false
        return result
    }

    suspend fun deviceToken(
        device: String,
        token: String,
        type: String,
        version: Int,
        os: String,
        appVersion: String,
    ): JtApiResult<ModelCommon> {
        showProgress.value = true
        val result = generalRepository.deviceToken(
            device = device,
            token = token,
            type = type,
            version = version,
            os = os,
            appVersion = appVersion
        )
        showProgress.value = false
        return result
    }


    suspend fun getExportsVideos():JtApiResult<ModelExpertVideo>{
        showProgress.value = true
        val result = generalRepository.getExportsVideos()
        showProgress.value = false
        return result
    }
}
