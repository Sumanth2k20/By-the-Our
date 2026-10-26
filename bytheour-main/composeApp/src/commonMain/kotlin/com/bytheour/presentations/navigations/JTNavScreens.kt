package com.bytheour.presentations.navigations

import kotlinx.serialization.Serializable

@Serializable
sealed class JTBtoNavScreens {

    @Serializable
    data object Splash : JTBtoNavScreens()

    @Serializable
    data class Login(val isProvider:Boolean): JTBtoNavScreens()

    @Serializable
    data class Otp(
        val mobile:String,
        val value:Int?,
        val firstName:String?,
        val lastName:String?,
        val userName:String?,
        val email:String?,
        val profile:String?,
    ):JTBtoNavScreens()

    @Serializable
    data object WellCome : JTBtoNavScreens()

    @Serializable
    data object AskLocationPermission:JTBtoNavScreens()

    @Serializable
    data object UserType:JTBtoNavScreens()

    @Serializable
    data class WebViewScreen(val webUrl:String,val title: String) : JTBtoNavScreens()

    @Serializable
    data class SignUp(
        val mobile:String,
        val value:String
    ): JTBtoNavScreens()

    @Serializable
    data class Location(val isAdmin:Boolean): JTBtoNavScreens()


    @Serializable
    data class Address(
        val fromHome:Boolean
    ): JTBtoNavScreens()


    @Serializable
    data class OneOnOneChat(
        val name:String,
        val roomId:String,
        val isArchive:Boolean,
        val fromChatSupport:String? =null,
        val profile:String?,
        val fromID: String?
    ) : JTBtoNavScreens()

    @Serializable
    data class Attachment(val message:String): JTBtoNavScreens()

    @Serializable
    data object ArchivedChat: JTBtoNavScreens()

    @Serializable
    data class JtMainScreen(
        val fromBookings:Boolean,
        val fromServices:Int? = null,
        val fromMessage:Boolean
    ): JTBtoNavScreens()

    @Serializable
    data object ExploreTabScreen: JTBtoNavScreens()

    @Serializable
    data object BookingsTabScreen: JTBtoNavScreens()

    @Serializable
    data object MoreTabScreen: JTBtoNavScreens()

    @Serializable
    data class ProfileEditScreen(
        val fromDialog:String,
    ): JTBtoNavScreens()

    @Serializable
    data object NotificationScreen: JTBtoNavScreens()


    @Serializable
    data object Gallery: JTBtoNavScreens()

    @Serializable
    data object AskPermission: JTBtoNavScreens()

    @Serializable
    data object ReportAnIssue: JTBtoNavScreens()

    @Serializable
    data class SelectLocation(
        val lat:String,
        val long:String,
        val value: String
    ): JTBtoNavScreens()

    @Serializable
    data class PaymentScreen(val bookingId: String): JTBtoNavScreens()

    @Serializable
    data object AddCardDetails: JTBtoNavScreens()




    @Serializable
    data object TransactionHistoryScreen: JTBtoNavScreens()

    @Serializable
    data object WalletScreen: JTBtoNavScreens()

    @Serializable
    data class TransactionDetailScreen(
         val bookingId:String
    ): JTBtoNavScreens()

    @Serializable
    data object ReviewAndRatingsScreen: JTBtoNavScreens()

    @Serializable
    data object PrivacyAndPolicy: JTBtoNavScreens()

    @Serializable
    data object DeleteScreen: JTBtoNavScreens()

    @Serializable
    data object MediationGuidelines: JTBtoNavScreens()


    @Serializable
    data object AdminUsers:JTBtoNavScreens()

    @Serializable
    data object TermsAndConditions: JTBtoNavScreens()

    @Serializable
    data object AboutScreen: JTBtoNavScreens()

    @Serializable
    data object Faq: JTBtoNavScreens()

    @Serializable
    data object InviteFriend: JTBtoNavScreens()

    @Serializable
    data object AdminReportsScreen: JTBtoNavScreens()

    @Serializable
    data object ServiceScreen: JTBtoNavScreens()

    @Serializable
    data object ServiceRequest: JTBtoNavScreens()


    @Serializable
    data object AdminManageServiceScreen: JTBtoNavScreens()

    @Serializable
    data object AdminWorkersScreen: JTBtoNavScreens()

    @Serializable
    data class AdminWorkersEditScreen(
        val userid: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val contactNumber: String,
        val profileImage: String,
        val accountType: String,
        val documentStatus: String
    ): JTBtoNavScreens()

    @Serializable
    data object AdminDownloadsScreen: JTBtoNavScreens()

    @Serializable
    data class AdminEditServiceRequestScreen(
        val id:Int,
        val title:String,
        val description:String,
        val category:String,
        val sequence:Int,
        val avatar:String,
        val serviceStatus:String,
    ): JTBtoNavScreens()

    @Serializable
    data class AdminEditManageServiceRequestScreen(
        val id:Int,
        val title:String,
        val description:String,
        val category:String,
        val sequence:Int,
        val avatar:String,
        val serviceStatus:String,
        val categoryId:Int
    ): JTBtoNavScreens()

    @Serializable
    data class UsersEdit(
        val id:String,
        val firstName: String? = "",
        val lastName :String? = "",
        val email :String? = "",
        val contactNumber :String? = "",
        val image:String? = ""
    ): JTBtoNavScreens()


    @Serializable
    data object TrendingServices: JTBtoNavScreens()

    @Serializable
    data class AvailableServices(
        val id :String,
        val title:String,
    ): JTBtoNavScreens()

    @Serializable
    data class GooglePlaces(val searchText:String): JTBtoNavScreens()

    @Serializable
    data object ServicesTabScreen: JTBtoNavScreens()

    @Serializable
    data object SelectCategory:JTBtoNavScreens()

    @Serializable
    data class SelectService( val id :String, val title: String):JTBtoNavScreens()

    @Serializable
    data class AddService(
        val navigationValue:String,
        val serviceId: String,
        val serviceTitle: String,
        val serviceDescription: String,
        val image: String,
        val hourlyRate:String,
        val minHours:String,
        val experience:String,
        val description:String,
        val isPremiseAvailable:String,
        val catId:String,
        val radius:String,
        val id: String
    ): JTBtoNavScreens()

    @Serializable
    data class BookingInfo(
        val bookingDataJson: String,
        val  bookingId: String
    ): JTBtoNavScreens()


    @Serializable
    data class CancelBooking(
        val id :String,
        val cancelAfterStartTime:String,
        val providerStartTime :String,
        val date:String
    ):JTBtoNavScreens()


    @Serializable
    data class MediationScreen(val id: String):JTBtoNavScreens()

    @Serializable
    data object ProviderBookingScreen: JTBtoNavScreens()

    @Serializable
    data class ProviderCancelReasonScreen(
        val id: String
    ) : JTBtoNavScreens()

    @Serializable
    data class SendEstimateScreen(
        val id: String,
        val time:String,
        val rate:String,
    ):JTBtoNavScreens()

    @Serializable
    data class ProviderBookingInfo(
        val bookingDataJson: String,
        val bookingId:String
    ): JTBtoNavScreens()

    @Serializable
    data class MediationRefundScreens(
        val bookingId: String,
        val amount:String,
        val total:String
    ): JTBtoNavScreens()

    @Serializable
    data class  SelectDateOfService(
        val serviceId:String,
        val title:String,
        val description: String,
        val image:String,
        val fromInspection:Boolean,
        val inspectionBookingId:String? = null,
        val inspectionRate:String? = null
    ): JTBtoNavScreens()

    @Serializable
    data class BookingRequestSentScreen(
        val title:String,
        val address: String,
        val image:String,
        val bookingId:String
    ): JTBtoNavScreens()

    @Serializable
    data class ServiceRequestScreen(
        val catId:String
    ): JTBtoNavScreens()

    @Serializable
    data class ViewProfile(
        val bookingDataJson: String
    ):JTBtoNavScreens()

    @Serializable
    data object ChatSupportScreen: JTBtoNavScreens()

    @Serializable
    data object AllTicketsScreen: JTBtoNavScreens()

    @Serializable
    data class DetailsScreen(
        val bookingId: String
    ): JTBtoNavScreens()


    @Serializable
    data class ChatSupport(val id:String):JTBtoNavScreens()
    @Serializable
    data object NotificationListScreen: JTBtoNavScreens()

    @Serializable
    data class ImageUploadScreen(
        val id:String,
        val value:String,
        val planId:String
    ): JTBtoNavScreens()

    @Serializable
    data object Documents: JTBtoNavScreens()

    @Serializable
    data class ImageFullScreen(
        val image:String,
        val desc:String
    ): JTBtoNavScreens()

    @Serializable
    data object CustomerCategory:JTBtoNavScreens()

    @Serializable
    data object BankDetailsScreen:JTBtoNavScreens()


    @Serializable
    data class CustomerRentals(val id:String, val title:String):JTBtoNavScreens()

    @Serializable
    data class SelectPlanScreen(
        val serviceId:String,
    ):JTBtoNavScreens()


    @Serializable
    data class BillingSummary(val id:String,val fromInspection:Boolean):JTBtoNavScreens()

    @Serializable
    data class InspectionReport(
        val providerName:String,
        val title:String,
        val image:String,
        val bookingId :String
    ):JTBtoNavScreens()

    @Serializable
    data class InspectionProceedingJob(
        val title:String,
        val image:String,
        val name: String,
        val id :String
    ):JTBtoNavScreens()

    @Serializable
    data class InspectionRequestSentScreen(
        val title:String,
        val address: String,
        val image:String,
        val bookingId:String,
    ): JTBtoNavScreens()

    @Serializable
    data class InspectionBillingScreen(
        val title:String,
        val address: String,
        val image:String,
        val bookingId:String,
    ) :JTBtoNavScreens()

    @Serializable
    data class InspectionDecline(
        val id :String
    ):JTBtoNavScreens()
}

