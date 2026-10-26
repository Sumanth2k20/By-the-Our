package com.bytheour.utils

import com.bytheour.core.JtApiResult
import com.bytheour.useCases.CommonUseCases


class FileUpload(
    private val commonUseCase: CommonUseCases
) {

    suspend fun uploadImage(
        file: ByteArray,
        useChatAttachment: Boolean = false,
        filename: (String) -> Unit = {},
        onMessage: (String) -> Unit = {},
        onSuccess: () -> Unit = {},
        showSuccessMessage: Boolean = true
    ) {
        val apiResult = if (useChatAttachment) {
            commonUseCase.generateChatAttachmentUrl(
                contentType = "image/jpeg",
                extension = "jpg",
            )
        } else {
            commonUseCase.gerProfileUrl(
                contentType = "image/jpeg",
                extension = "jpg",
            )
        }

        when (apiResult) {
            is JtApiResult.Success -> {
                val uploadUrl = apiResult.data.data.url
                val fileName = apiResult.data.data.filename
                if (uploadUrl.isEmpty()) {
                    onMessage(apiResult.data.meta.message ?: "")
                }else{
                    uploadDoc(
                        file = file,
                        uploadUrl = uploadUrl
                    )
                    filename(fileName)
                    if(showSuccessMessage) {
                        onMessage(apiResult.data.meta.message ?: "")
                    }
                    onSuccess()
                }

            }

            is JtApiResult.Failure -> {
                onMessage("Image Upload failed. Please try again.")
            }
        }
    }
    suspend fun uploadGalleryImage(
        file: ByteArray,
        filename:(String) -> Unit = {},
        onMessage: (String) -> Unit = {},
        onSuccess:() ->Unit = {},
        showSuccessMessage:Boolean = true
    ) {
        val getProfileUrl = commonUseCase.getGalleryImageUrl(
            contentType = "image/jpeg",
            extension = "jpg",
        )

        when (getProfileUrl) {
            is JtApiResult.Success -> {
                val uploadUrl = getProfileUrl.data.data.url
                val fileName = getProfileUrl.data.data.filename
                if (uploadUrl.isEmpty()){
                    onMessage(getProfileUrl.data.meta.message?:"")
                }else{
                    uploadDoc(
                        file = file,
                        uploadUrl = uploadUrl
                    )
                    filename(fileName)
                    if(showSuccessMessage) {
                        onMessage(getProfileUrl.data.meta.message ?: "")
                    }
                    onSuccess()
                }
            }
            is JtApiResult.Failure -> {
                onMessage("Image Upload Is failed try again")
            }
        }
    }
    suspend fun uploadDoc(
        file: ByteArray,
        uploadUrl: String
    ) {
        commonUseCase.uploadAwsDocSafe(
            uploadUrl = uploadUrl,
            fileBytes = file
        )
    }


}

