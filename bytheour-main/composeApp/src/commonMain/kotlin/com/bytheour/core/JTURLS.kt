package com.bytheour.core

import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.API_VERSION
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_GENERAL
import com.bytheour.utils.TYPE_PROVIDER
import com.russhwolf.settings.Settings

object JTURLS {

    private const val PROD_URL = "https://bytheour.com/api/"
    private const val DEV_URL ="http://3.13.228.170/api/"
    private const val BASEURL = DEV_URL


    const val BASE_IMAGE_URL = "https://stagingbythehour.s3.us-east-2.amazonaws.com/"
    const val CHAT_BASE_URL: String = "http://3.13.228.170:8080/"
    var ROLETYPE = TYPE_CUSTOMER
    private val getTokenFromPreference:SharedPreferenceUseCase = SharedPreferenceUseCase(Settings())

    fun token() = getTokenFromPreference.getToken()


    // General URLs
    const val MEDIATION_GUIDELINES = BASEURL + TYPE_GENERAL + API_VERSION + "get-mediation-guidelines"
    const val APP_STATUS = BASEURL + TYPE_GENERAL + API_VERSION + "app-status"
    const val PRIVACY_POLICY = BASEURL + TYPE_GENERAL + API_VERSION + "get-privacy-policy"
    fun CHECK_USERNAME() = BASEURL + ROLETYPE + API_VERSION + "general/check/username"
    fun DELETEACCOUNT() = BASEURL + ROLETYPE + API_VERSION + "delete/account"
    fun  CHECK_EMAIL() = BASEURL + ROLETYPE + API_VERSION + "general/check/email"
    fun LOGIN() = BASEURL + ROLETYPE + API_VERSION + "general/check/contact"
    fun GOOGLE_PLACES() = "https://maps.googleapis.com/maps/api/place/autocomplete/json"
    fun GOOGLE_PLACE_DETAILS()=("https://maps.googleapis.com/maps/api/place/details/json")
    const val GET_APP_VERSION = BASEURL + TYPE_GENERAL + API_VERSION + "get-app-versions"
    const val FAQ = BASEURL + TYPE_GENERAL + API_VERSION + "get-faqs"
    const val CANCEL_REASONS = BASEURL + TYPE_GENERAL + API_VERSION + "get-reasons"
    fun loginUserLogin() = BASEURL + ROLETYPE + API_VERSION + "login"
    fun LOG_OUT() = BASEURL + ROLETYPE + API_VERSION + "logout"
    fun register() = BASEURL + ROLETYPE + API_VERSION + "register"
    fun saveProfile() = BASEURL + ROLETYPE + API_VERSION + "switch-account-save-profile"
    fun taxClassificationUrl() = BASEURL + ROLETYPE + API_VERSION + "manage/tax-classification"
    fun updateVisibility() = BASEURL + ROLETYPE + API_VERSION + "update/visibility-status"
    fun NOTIFICATION_STATUS() = BASEURL + ROLETYPE + API_VERSION + "update/notification-status"
    fun GET_PROFILE() = BASEURL + ROLETYPE + API_VERSION + "get/profile"
    fun UPDATE_PROFILE ()= BASEURL + ROLETYPE + API_VERSION + "update/profile"


    fun REPORT_ISSUE() = BASEURL + ROLETYPE + API_VERSION + "app/report-issue"

    fun DEVICE_TOKEN()=  BASEURL + ROLETYPE + API_VERSION +"add/device-token"

    // Admin
    fun ADMIN_DASHBOARD_COUNT() = BASEURL + ROLETYPE + API_VERSION + "admin/dashboard-counts"
    fun ADMIN_DOWNLOAD() = BASEURL + ROLETYPE + API_VERSION + "admin/downloads"
    fun ADMIN_EDIT_SERVICE_REQUEST_LIST() = BASEURL + ROLETYPE + API_VERSION + "admin/edit-service-request"
    fun ADMIN_EDIT_SERVICE_MANAGE() = BASEURL + ROLETYPE + API_VERSION + "admin/edit-service"

    // AdminModule
    fun USERS_LIST() = BASEURL + ROLETYPE + API_VERSION + "admin/user-list"
    fun ADMIN_CHANGE_USER_STATUS() = BASEURL + ROLETYPE + API_VERSION + "admin/change-user-status"
    fun ADMIN_DELETE_USER() = BASEURL + ROLETYPE + API_VERSION + "admin/delete-user"
    fun ADMIN_GET_USER_DATA() = BASEURL + ROLETYPE + API_VERSION + "admin/get-user-data"
    fun ADMIN_EDIT_USER() = BASEURL + ROLETYPE + API_VERSION + "admin/edit-user"
    fun ADMIN_SERVICE_LIST() = BASEURL + ROLETYPE + API_VERSION + "admin/service-list"
    fun ADMIN_CHANGE_SERVICE_STATUS() = BASEURL + ROLETYPE + API_VERSION + "admin/change-service-status"
    fun ADMIN_DELETE_SERVICE() = BASEURL + ROLETYPE + API_VERSION + "admin/delete-service"
    fun ADMIN_WORKER_LIST() = BASEURL + ROLETYPE + API_VERSION + "admin/worker-list"
    fun ADMIN_CHANGE_WORKER_STATUS() = BASEURL + ROLETYPE + API_VERSION + "admin/change-worker-status"
    fun ADMIN_DELETE_WORKER() = BASEURL + ROLETYPE + API_VERSION + "admin/delete-worker"
    fun ADMIN_GET_WORKER_DATA() = BASEURL + ROLETYPE + API_VERSION + "admin/get-worker-data"
    fun ADMIN_EDIT_WORKER() = BASEURL + ROLETYPE + API_VERSION + "admin/edit-worker"
    fun ADMIN_VERIFICATION_STATUS() = BASEURL + ROLETYPE + API_VERSION + "admin/change-worker-verification-status"
    fun ADMIN_SERVICE_REQUEST_LIST() = BASEURL + ROLETYPE + API_VERSION + "admin/service-request-list"
    fun ADMIN_MANAGE_SERVICE_LIST() = BASEURL + ROLETYPE + API_VERSION + "admin/service-list"
    fun GET_DASHBOARD_DETAILS() = BASEURL + ROLETYPE + API_VERSION + "get/dashboard"

    // profile
    fun GET_PROFILE_URL_USER() = BASEURL + ROLETYPE + API_VERSION + "generate-url/profile"

    // Bookings
    fun UPCOMING_LIST() = BASEURL + ROLETYPE + API_VERSION + "get/bookings/upcoming"
    fun ONGOING_LIST() = BASEURL + ROLETYPE + API_VERSION + "get/bookings/ongoing"
    fun PENDING_LIST() = BASEURL + ROLETYPE + API_VERSION + "get/bookings/request"
    fun COMPLETE_BOOKINGS ()= BASEURL + ROLETYPE + API_VERSION + "get/bookings/completed"
    fun CANCELLED_BOOKINGS() = BASEURL + ROLETYPE + API_VERSION + "get/bookings/cancelled"
    fun raiseMediation() = BASEURL + ROLETYPE + API_VERSION + "request/raise-mediation"
    fun ACCEPT_MEDIATION() = BASEURL + ROLETYPE + API_VERSION + "request/accept-mediation"
    fun manageAddBookings()= BASEURL + ROLETYPE + API_VERSION + "create/booking"
    fun getWorkerSpecificServices() = BASEURL + ROLETYPE + API_VERSION + "get/worker-specific-services"
    fun getPromoCodesList() = BASEURL + ROLETYPE + API_VERSION + "get/promo-codes"


    fun PROVIDER_GET_SERVICE() = BASEURL + ROLETYPE + API_VERSION + "get/services"

    fun TRANSACTION_LIST() = BASEURL + ROLETYPE + API_VERSION + "get/transaction-list"
    fun BOOKING_DETAILS() = BASEURL + ROLETYPE + API_VERSION + "get/booking/details"

    fun PROVIDER_STRIPE_CONNECT_WITHDRAW() = BASEURL + ROLETYPE + API_VERSION + "stripe/connect/withdrawal"

    fun PROVIDER_GET_DOCUMENT() = BASEURL + ROLETYPE + API_VERSION + "get/documents"

    // ChatSupport
    fun SUPPORT_TICKET() = BASEURL + ROLETYPE + API_VERSION + "get/support-ticket"
    fun ADD_SUPPORT_TICKET ()= BASEURL + ROLETYPE + API_VERSION + "add/support-ticket"
    fun GET_SUPPORT_HISTORY ()= BASEURL + ROLETYPE + API_VERSION + "get/support-history"
    fun GET_SUPPORT_BOOKING ()= BASEURL + ROLETYPE + API_VERSION + "get/support-bookings"
    fun CLOSE_SUPPORT_TICKET ()= BASEURL + ROLETYPE + API_VERSION + "close/support-ticket"

    fun GET_RATING() = BASEURL + ROLETYPE + API_VERSION + "get/ratings"
    fun CANCEL_REQUEST_BOOKING() = BASEURL + ROLETYPE + API_VERSION + "request/cancel-booking"
    const val CUSTOMER_GET_PAYMENT = BASEURL + TYPE_CUSTOMER + API_VERSION + "payment/get-key"
    const val CUSTOMER_START_BOOKING = BASEURL + TYPE_CUSTOMER + API_VERSION + "request/start-booking"
    const val CUSTOMER_REQUEST_COMPLETE_BOOKING = BASEURL + TYPE_CUSTOMER + API_VERSION + "request/complete-booking"

    // Notification
    fun getNotificationList() = BASEURL + ROLETYPE + API_VERSION + "get/notification-list"
    fun GET_SERVICE() = BASEURL + TYPE_GENERAL + API_VERSION + "get-services"
    const val PROVIDER_ACCEPT_BOOKINGS = BASEURL + TYPE_PROVIDER + API_VERSION + "update/booking/accept"
    const val PROVIDER_DECLINE_BOOKINGS = BASEURL + TYPE_PROVIDER + API_VERSION + "update/booking/decline"

    fun PROVIDER_SEND_ESTIMATION() = BASEURL + ROLETYPE + API_VERSION + "send/quotation"
    fun CREATE_CHAT_ROOM() = BASEURL + ROLETYPE + API_VERSION + "create/chat-room"
    fun PROVIDER_ADD_RATINGS() = BASEURL + ROLETYPE + API_VERSION + "add/ratings"
    fun PROVIDER_UPDATE_RATINGS() = BASEURL + ROLETYPE + API_VERSION + "update/ratings"
    fun START_JOB() = BASEURL + ROLETYPE + API_VERSION + "request/start-booking"
    fun END_JOB() = BASEURL + ROLETYPE + API_VERSION + "request/complete-booking"
    fun searchServices() = BASEURL + ROLETYPE + API_VERSION + "search/services"
    fun workerGetBookingRequest() = BASEURL + ROLETYPE + API_VERSION + "get/booking-request"
    fun updateBookingRequest() = BASEURL + ROLETYPE + API_VERSION + "update/booking-request/accept"
    fun rejectBookingRequest() = BASEURL + ROLETYPE + API_VERSION + "update/booking-request/reject"
    fun getExpertsVideos() = BASEURL  + ROLETYPE + API_VERSION + "get/expert-videos"



    // BankDetails
    fun SWITCH_ACCOUNT() = BASEURL + ROLETYPE + API_VERSION + "switch-account"
    fun SWITCH_ACCOUNT_STATUS() = BASEURL + ROLETYPE + API_VERSION + "switch-account/status"
    fun INVITE_CODE() = BASEURL + ROLETYPE + API_VERSION + "get/invite-code"

    // Address
    fun GET_ADDRESS() = BASEURL + ROLETYPE + API_VERSION + "get/address"
    fun CUSTOMER_DEFAULT_ADDRESS() = BASEURL + ROLETYPE + API_VERSION + "default/address"
    fun CUSTOMER_ADD_ADDRESS() = BASEURL + ROLETYPE + API_VERSION + "manage/address"
    fun CUSTOMER_DELETE_ADDRESS ()= BASEURL + ROLETYPE + API_VERSION + "delete/address"
    fun addCard ()= BASEURL + ROLETYPE + API_VERSION + "payment/add-card"
    fun getCards ()= BASEURL + ROLETYPE + API_VERSION + "payment/get-cards"
    fun manageQuotation() = BASEURL + ROLETYPE + API_VERSION + "manage/quotation"

    // Chats
    fun GET_CHAT_LIST() = BASEURL + ROLETYPE + API_VERSION + "get/chat-rooms"
    fun GET_CHAT_MESSAGE() = BASEURL + ROLETYPE + API_VERSION + "get/chat-message"
    fun GET_ARCHIVE_CHAT() = BASEURL + ROLETYPE + API_VERSION + "get/archive-chat"
    fun CHAT_ARCHIVE()= BASEURL + ROLETYPE + API_VERSION + "chat/archive"
    fun generateChatUrl() = BASEURL + ROLETYPE + API_VERSION + "generate-url/chat-message"

    //Services
    fun TRENDING_SERVICES() = BASEURL+ TYPE_CUSTOMER + API_VERSION + "get/trending-services"
    fun serviceDetails() = BASEURL + ROLETYPE + API_VERSION + "get/service-details"
    fun getServiceProviderDetails() = BASEURL + ROLETYPE + API_VERSION + "get/service-providers"
    fun getWorkersRatings() = BASEURL + ROLETYPE + API_VERSION + "get/worker-ratings"


    fun GET_CATEGORY() =BASEURL+ ROLETYPE + API_VERSION +"get/categories"
    fun  DELETE_SERVICE()=BASEURL+ ROLETYPE + API_VERSION +"delete/services"
    fun  ADD_SERVICE ()=BASEURL+ ROLETYPE + API_VERSION +"add/services"
    fun   MANAGE_COM_RATE()= BASEURL+ ROLETYPE + API_VERSION +"manage/common-rate"
    fun   MANAGE_SERVICES_BY_WORKER () =BASEURL+ ROLETYPE + API_VERSION +"manage-services-by-worker"



    fun GALLERY_PAYMENT_INTENT() = BASEURL + ROLETYPE + API_VERSION + "gallery/init-payment"
    fun GALLERY_GET_CARDS() = BASEURL + ROLETYPE + API_VERSION + "gallery/get-cards"
    fun GALLERY_EPHEMERAL_KEY() = BASEURL + ROLETYPE + API_VERSION + "gallery/get-payment-key"
    fun GALLERY_COMPLETE_PAYMENT() = BASEURL + ROLETYPE + API_VERSION + "gallery/complete-payment"

    fun GALLERY_GET_PLANS() = BASEURL + ROLETYPE + API_VERSION + "gallery/get-plans"
    fun GALLERY_GENERATE_URL() = BASEURL + ROLETYPE + API_VERSION + "generate-url/gallery"
    fun GALLERY_ADD_IMAGE() = BASEURL + ROLETYPE + API_VERSION + "gallery/add-image"
    fun GALLERY_DELETE_IMAGE() = BASEURL + ROLETYPE + API_VERSION + "gallery/delete-image"
    fun GALLERY_BOOKINGS() = BASEURL + ROLETYPE + API_VERSION + "gallery/bookings"

    // Stripe Module
    fun ephemeralKey() = BASEURL+ ROLETYPE + API_VERSION +"payment/get-key"
    fun doPayment() = BASEURL+ ROLETYPE + API_VERSION +"payment/init-payment"
    fun completePayment() = BASEURL+ ROLETYPE + API_VERSION +"payment/complete-payment"
    fun bankDetailsStripeConnect () = BASEURL+ ROLETYPE + API_VERSION +"stripe/connect/manage"
    fun removeBankDetailsStripeConnect () = BASEURL+ ROLETYPE + API_VERSION +"stripe/connect/delete"

    fun stripeVerificationUrl() = BASEURL+ ROLETYPE + API_VERSION +"stripe/verify-account/manage"
    fun stripeVerificationStatus() = BASEURL+ ROLETYPE + API_VERSION +"stripe/verify-account/check-status"


    //inspection-booking flow
    fun inspectionCreate()= BASEURL+ ROLETYPE+ API_VERSION+"create/inspection-booking"
    fun inspectionReport() = BASEURL + ROLETYPE + API_VERSION + "request/inspection-report"
    fun acceptInspectionReport() = BASEURL + ROLETYPE + API_VERSION + "request/accept-inspection-report"
    fun rejectInspectionReport() = BASEURL + ROLETYPE + API_VERSION + "request/reject-inspection-report"

}
