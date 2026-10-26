package com.bytheour.utils



interface ShareService {
    fun share(title: String, url: String, code: String?)
}


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ShareServiceProvider() {
    fun getShareService(): ShareService
}