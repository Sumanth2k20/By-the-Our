package com.bytheour.presentations.navigations

import androidx.navigation.NavController
import com.bytheour.presentations.screens.tabs.bookings.BookingData
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.BookingDataProvider
import kotlinx.serialization.json.Json

class JTBtoNavActions(private val navController: NavController) {

    fun navigateBack() {
        navController.popBackStack()
    }

//    fun navigateBackD(result: String? = null) {
//        if (result != null) {
//            navController.previousBackStackEntry
//                ?.savedStateHandle
//                ?.set("fromDialog", result)
//        }
//        navController.popBackStack()
//    }
//
//    fun navigateBackS(result: String? = null) {
//        if (result != null) {
//            navController.previousBackStackEntry
//                ?.savedStateHandle
//                ?.set("fromSignUp", result)
//        }
//        navController.popBackStack()
//    }

    fun navigateBackClear() {
        navController.currentBackStackEntry?.destination?.route?.let {
            navController.popBackStack(
                route = it,
                inclusive = true
            )
        }
    }

    fun navigateToLogin(isProvider:Boolean){
        navController.navigate(JTBtoNavScreens.Login(isProvider))
    }

    fun navigateToOtp(
        mobile:String,
        value:Int?,
        firstName:String,
        lastName:String,
        userName:String,
        email:String,
        profile:String,
    ){
        navController.navigate(JTBtoNavScreens.Otp(
            mobile,
            value,
            firstName,
            lastName,
            userName,
            email,
            profile
        ))
    }

    fun navigateToSignUp(mobile:String){
        navController.navigate(JTBtoNavScreens.SignUp(mobile,""))
    }
    fun navigateToSignUpcClear(mobile:String,from:String){
        navController.navigate(JTBtoNavScreens.SignUp(mobile,from))
    }

    fun navigateToLocation(isAdmin:Boolean?= true){
        navController.navigate(JTBtoNavScreens.Location(isAdmin?:false)){
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToAskLocationPermission(){
        navController.navigate(JTBtoNavScreens.AskLocationPermission)
    }


    fun navigateToUserType(){
        navController.navigate(JTBtoNavScreens.UserType)
    }
    fun navigateToUserTypeFromMore(){
        navController.navigate(JTBtoNavScreens.UserType) {
            popUpTo(0) { inclusive = true } // Clears entire back stack
            launchSingleTop = true
        }
    }
    fun navigateToNotification(){
        navController.navigate(JTBtoNavScreens.NotificationScreen)
    }

    fun navigateToDeleteScreen(){
        navController.navigate(JTBtoNavScreens.DeleteScreen)
    }

    fun navigateToProfileEditScreen(value:String){
        navController.navigate(JTBtoNavScreens.ProfileEditScreen(value))
    }

    fun navigateToMediationGuidelines(){
        navController.navigate(JTBtoNavScreens.MediationGuidelines)
    }
    fun navigatePermissions(){
        navController.navigate(JTBtoNavScreens.AskPermission)
    }

    fun navigateAddresses(fromHome:Boolean){
        navController.navigate(JTBtoNavScreens.Address(fromHome))
    }
    fun navigateToReportAnIssueScreen(){
        navController.navigate(JTBtoNavScreens.ReportAnIssue)
    }



    fun navigateToWellCome(){
        navController.navigate(JTBtoNavScreens.WellCome)
    }

    fun navigateToOneOnOneChat(name:String,roomId:String,isArchive:Boolean,supportId:String,profile: String?,fromID:String?){
        navController.navigate(JTBtoNavScreens.OneOnOneChat(name,roomId,isArchive,supportId,profile?:"",fromID))
    }

    fun navigateToArchivedChat(){
        navController.navigate(JTBtoNavScreens.ArchivedChat)
    }

    fun navigateToAttachment(image:String){
        navController.navigate(JTBtoNavScreens.Attachment(image))
    }

    fun navigateToMainScreen(fromBookings:Boolean,fromServices:Int,fromMessage:Boolean){
        navController.navigate(JTBtoNavScreens.JtMainScreen(fromBookings,fromServices,fromMessage)){
            popUpTo(0) { inclusive = true } // Clears entire back stack
            launchSingleTop = true
        }
    }

    fun navigateToPrivacyAndPolicy(){
        navController.navigate(JTBtoNavScreens.PrivacyAndPolicy)
    }
    fun navigateToTermsAndConditionsScreen(){
        navController.navigate(JTBtoNavScreens.TermsAndConditions)
    }
    fun navigateToAboutBTOScreen(){
        navController.navigate(JTBtoNavScreens.AboutScreen)
    }


    fun navigateToFaq(){
        navController.navigate(JTBtoNavScreens.Faq)
    }

    fun navigateToInviteFriendsScreen(){
        navController.navigate(JTBtoNavScreens.InviteFriend)
    }

    fun navigateToAdminManageServiceScreen(){
        navController.navigate(JTBtoNavScreens.AdminManageServiceScreen)
    }

    fun navigateToAdminUserScreen(){
        navController.navigate(JTBtoNavScreens.AdminUsers)
    }

    fun navigateToUsersEditScreen(id:String,firstName:String,lastName:String,email:String,contactNumber:String,image: String){
        navController.navigate(JTBtoNavScreens.UsersEdit(id,firstName,lastName,email,contactNumber,image))
    }

    fun navigateToGooglePlacesScreen(searchText:String){
        navController.navigate(JTBtoNavScreens.GooglePlaces(searchText))
    }


    fun navigateToServiceRequest(){
        navController.navigate(JTBtoNavScreens.ServiceRequest)
    }
    fun navigateToServiceEditScreen(
        id:Int,
        title:String,
        description:String,
        category: String,
        avatar:String,
        sequence:Int,
        serviceStatus:String
    ){
        navController.navigate(JTBtoNavScreens.AdminEditServiceRequestScreen(
            id,
            title,
            description,
            category,
            sequence,
            avatar,
            serviceStatus
        ))
    }

    fun navigateToManageServiceEditScreen(
        id:Int,
        title:String,
        description:String,
        category: String,
        avatar:String,
        sequence:Int,
        serviceStatus:String,
        categoryId:Int
    ){
        navController.navigate(JTBtoNavScreens.AdminEditManageServiceRequestScreen(
            id,
            title,
            description,
            category,
            sequence,
            avatar,
            serviceStatus,
            categoryId = categoryId
        ))
    }

    fun navigateToManageServiceRequest(){
        navController.navigate(JTBtoNavScreens.AdminManageServiceScreen)
    }

    fun navigateToWorkersScreen(){
        navController.navigate(JTBtoNavScreens.AdminWorkersScreen)
    }

    fun navigateToEditWorkersScreen(id: String,firstName:String,lastName:String,email:String,contactNumber:String,profileImage:String,accountType:String,documentStatus:String){
        navController.navigate(JTBtoNavScreens.AdminWorkersEditScreen(
            userid = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            contactNumber = contactNumber,
            profileImage = profileImage,
            accountType = accountType,
            documentStatus = documentStatus
        ))
    }


    fun navigateSelectLocation( lat: String,
                                long: String,
                                fromService:String) {
        navController.navigate(JTBtoNavScreens.SelectLocation(lat = lat, long = long,fromService))
    }


    fun navigateAddNewCard() {
        navController.navigate(JTBtoNavScreens.AddCardDetails)
    }

     fun navigateToCardList(bookingId:String){
        navController.navigate(JTBtoNavScreens.PaymentScreen(bookingId))
    }

    fun navigateToTrendingServicesScreen(){
        navController.navigate(JTBtoNavScreens.TrendingServices)
    }

    fun navigateToAvailableServicesScreen(id: String,title: String){
        navController.navigate(JTBtoNavScreens.AvailableServices(id,title))
    }

    fun navigateToBookingInfoScreen(bookingData: BookingData,bookingId: String) {
        val bookingDataJson = Json.encodeToString(bookingData)
        navController.navigate(JTBtoNavScreens.BookingInfo(bookingDataJson,bookingId))
    }

    fun navigateToProviderBookingInfoScreen(bookingData: BookingDataProvider,bookingId:String) {
        val bookingDataJson = Json.encodeToString(bookingData)
        navController.navigate(JTBtoNavScreens.ProviderBookingInfo(bookingDataJson,bookingId))
    }

    fun navigateToProviderCancelReasonScreen(id: String){
        navController.navigate(JTBtoNavScreens.ProviderCancelReasonScreen(id))
    }
    fun navigateToSendEstimateScreen(id: String,time:String, cost:String){
        navController.navigate(JTBtoNavScreens.SendEstimateScreen(id,time,cost))
    }

    fun navigateToCancelBookingScreen(id :String,cancelAfterStartTime:String, providerStartTime :String,date:String){
        navController.navigate(JTBtoNavScreens.CancelBooking(id,cancelAfterStartTime,providerStartTime,date))
    }

    fun navigateToMediationScreen(id:String){
        navController.navigate(JTBtoNavScreens.MediationScreen(id))
    }

    fun navigateToMediationRefundScreen(id:String,amount:String,total:String){
        navController.navigate(JTBtoNavScreens.MediationRefundScreens(id,amount,total))
    }
    fun navigateToDateSelectionScreen(serviceId:String,title:String,description: String,image:String,fromInspection:Boolean,inspectionBookingId:String?=null,inspectionRate:String?=null){
        navController.navigate(JTBtoNavScreens.SelectDateOfService(
            serviceId,
            title,
            description,
            image,
            fromInspection,
            inspectionBookingId,
            inspectionRate
        ))
    }

    fun navigateToBookingConfirmationScreen(title: String,address:String,image: String,bookingId:String){
        navController.navigate(JTBtoNavScreens.BookingRequestSentScreen(title,address,image,bookingId))
    }

    fun navigateToViewProfileScreen(bookingData: BookingData){
        val bookingDataJson = Json.encodeToString(bookingData)
        navController.navigate(JTBtoNavScreens.ViewProfile(bookingDataJson))
    }

    fun navigateSelectCategory(){
        navController.navigate(JTBtoNavScreens.SelectCategory)
    }
    fun navigateSelectCategoryClear(){
        navController.navigate(JTBtoNavScreens.SelectCategory) {
            popUpTo(navController.currentDestination?.id ?: return@navigate) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }


    fun navigateSelectService(id: String,title: String){
        navController.navigate(JTBtoNavScreens.SelectService(id,title))
    }

    fun navigateAddServiceScreen(serviceId: String, serviceName:String, serviceDescription:String, navigationValue:String, image: String, hourlyRate:String, minHour:String, experience:String, description: String, isPremiseAvailable:String, catId:String, radius:String, iD:String){
        navController.navigate(JTBtoNavScreens.AddService(
            navigationValue = navigationValue,
            serviceId = serviceId,
            serviceTitle = serviceName,
            serviceDescription = serviceDescription,
            image =     image,
            hourlyRate = hourlyRate,
            minHours = minHour,
            experience = experience,
            description = description,
            isPremiseAvailable = isPremiseAvailable,
            catId = catId,
            radius = radius,
            id = iD
        )
        )
    }



    fun navigateTransactionHistoryScreen(){
        navController.navigate(JTBtoNavScreens.TransactionHistoryScreen)
    }

    fun navigateWalletScreen(){
        navController.navigate(JTBtoNavScreens.WalletScreen)
    }

    fun navigateToTransactionDetailScreen(id:String){
        navController.navigate(JTBtoNavScreens.TransactionDetailScreen(id))
    }

    fun navigateToReviewAndRatingsScreen(){
        navController.navigate(JTBtoNavScreens.ReviewAndRatingsScreen)
    }

    fun navigateToChatSupportScreen(){
        navController.navigate(JTBtoNavScreens.ChatSupportScreen)

    }


    fun navigateToAllTicketsScreen(){
        navController.navigate(JTBtoNavScreens.AllTicketsScreen)
    }

    fun navigateToDetailsScreen(id:String){
        navController.navigate(JTBtoNavScreens.DetailsScreen(id))
    }

    fun navigateToNotificationListScreen(){
        navController.navigate(JTBtoNavScreens.NotificationListScreen)
    }


    fun navigateToChatSupport(id:String){
        navController.navigate(JTBtoNavScreens.ChatSupport(id))
    }

    fun navigateToImageUploadScreen(id: String,value:String,planId:String){
        navController.navigate(JTBtoNavScreens.ImageUploadScreen(id,value,planId))
    }
    fun navigateToImageUploadScreenClear(id: String){
        navController.navigate(JTBtoNavScreens.ImageUploadScreen(id,"","")) {
            popUpTo(navController.currentDestination?.id ?: return@navigate) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun navigateToCustomerCategory(){
        navController.navigate(JTBtoNavScreens.CustomerCategory)
    }

    fun navigateToDocumentsScreen(){
        navController.navigate(JTBtoNavScreens.Documents)
    }
    fun navigateToImageFullScreen(
        image: String,
        description: String,
    ){
        navController.navigate(JTBtoNavScreens.ImageFullScreen(
            image = image,
            desc = description,
        ))
    }

    fun navigateToCustomerRentals(id:String,title: String){
        navController.navigate(JTBtoNavScreens.CustomerRentals(id ,title))
    }
    fun navigateToBankDetailsScreen(){
        navController.navigate(JTBtoNavScreens.BankDetailsScreen)
    }
    fun navigateToSelectPlanScreen(id:String) {
        navController.navigate(JTBtoNavScreens.SelectPlanScreen(id))
    }

    fun navigateToBillingSummary(id:String,fromInspection: Boolean){
        navController.navigate(JTBtoNavScreens.BillingSummary(id,fromInspection))
    }

    fun navigateToInspectionReport(name:String,title:String,image:String,bookingId: String){
        navController.navigate(JTBtoNavScreens.InspectionReport(name,title,image,bookingId))
    }
    fun navigateToInspectionProceedingJob(title: String,image: String,name: String,id: String){
        navController.navigate(JTBtoNavScreens.InspectionProceedingJob(title,image,name,id))
    }

    fun navigateToInspectionRequestSentScreen(address:String,title: String,image: String,bookingId:String){
        navController.navigate(JTBtoNavScreens.InspectionRequestSentScreen(address,title,image,bookingId))
    }

    fun navigateToInspectionBillingDetails(address:String,title: String,image: String,bookingId:String){
        navController.navigate(JTBtoNavScreens.InspectionBillingScreen(address = address,title = title,image = image,bookingId = bookingId))
    }


    fun navigateToWebViewScreen(webUrl:String,title: String){
        navController.navigate(JTBtoNavScreens.WebViewScreen(webUrl,title))
    }

    fun navigateToWebViewScreenClear(webUrl:String, title: String){
        navController.navigate(JTBtoNavScreens.WebViewScreen(webUrl,title)) {
            popUpTo(navController.currentDestination?.id ?: return@navigate) {
                inclusive = true
            }
            launchSingleTop = true
        }

    }


    fun navigateToInspectionDecline(id:String){
        navController.navigate(JTBtoNavScreens.InspectionDecline(id))
    }


}
