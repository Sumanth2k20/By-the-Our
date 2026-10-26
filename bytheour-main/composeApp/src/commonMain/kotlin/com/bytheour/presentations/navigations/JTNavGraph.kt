package com.bytheour.presentations.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bytheour.JtLocationEnableScreen
import com.bytheour.presentations.screens.JTBtoMainScreen
import com.bytheour.presentations.screens.JTUserTypeScreen
import com.bytheour.presentations.screens.location.JTLocationFetchingScreen
import com.bytheour.presentations.screens.login.JTAskPermission
import com.bytheour.presentations.screens.login.JTGalleryAndCamera
import com.bytheour.presentations.screens.login.JTLoginScreen
import com.bytheour.presentations.screens.otp.JTOtpScreen
import com.bytheour.presentations.screens.services.AvailableServices
import com.bytheour.presentations.screens.services.TrendingServices
import com.bytheour.presentations.screens.signup.JTSignUpScreen
import com.bytheour.presentations.screens.splash.JTSplashScreen
import com.bytheour.presentations.screens.tabs.bookings.AddCardDetailsScreen
import com.bytheour.presentations.screens.tabs.bookings.BillingSummaryScreen
import com.bytheour.presentations.screens.tabs.bookings.BookingData
import com.bytheour.presentations.screens.tabs.bookings.BookingInfoScreen
import com.bytheour.presentations.screens.tabs.bookings.CancelBookingReasonsScreen
import com.bytheour.presentations.screens.tabs.bookings.ChatSupportScreen
import com.bytheour.presentations.screens.tabs.bookings.JtBookingsTabScreen
import com.bytheour.presentations.screens.tabs.bookings.PaymentScreen
import com.bytheour.presentations.screens.tabs.bookings.RequestMediationScreen
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionProceedingJob
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionReportScreen
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionRequestSentScreen
import com.bytheour.presentations.screens.tabs.bookings.ViewProviderProfileScreen
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionBillingSummaryScreen
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionDeclineScreen
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.JtProviderBookingsTabScreen
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.MediationRefundingScreen
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.ProviderBookingInfoScreen
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.SendEstimateScreen
import com.bytheour.presentations.screens.tabs.explore.CustomerCategoriesScreen
import com.bytheour.presentations.screens.tabs.explore.CustomerRentalsScreen
import com.bytheour.presentations.screens.tabs.explore.JtExploreTabScreen
import com.bytheour.presentations.screens.tabs.explore.notification.NotificationListScreen
import com.bytheour.presentations.screens.tabs.explore.searchingAndRequestSentScreens.BookingConfirmationScreen
import com.bytheour.presentations.screens.tabs.explore.searchingAndRequestSentScreens.SelectionDateOfService
import com.bytheour.presentations.screens.tabs.messages.ArchivedChatScreen
import com.bytheour.presentations.screens.tabs.messages.ChatAttachmentDisplayScreen
import com.bytheour.presentations.screens.tabs.messages.OneOnOneChatScreen
import com.bytheour.presentations.screens.tabs.moreTab.delete.DeleteScreen
import com.bytheour.presentations.screens.tabs.moreTab.FAQScreen
import com.bytheour.presentations.screens.tabs.moreTab.JTMoreTabScreen
import com.bytheour.presentations.screens.tabs.moreTab.PrivacyAndPolicyScreen
import com.bytheour.presentations.screens.tabs.moreTab.aboutBtoScreen.JtAboutScreen
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTAddressList
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTGooglePlaceSearch
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTSelectLocation
import com.bytheour.presentations.screens.tabs.moreTab.bankDetails.BankDetailsScreen
import com.bytheour.presentations.screens.tabs.moreTab.chatSupport.AllTicketsScreen
import com.bytheour.presentations.screens.tabs.moreTab.chatSupport.ChatSupportScreen
import com.bytheour.presentations.screens.tabs.moreTab.chatSupport.DetailsScreen
import com.bytheour.presentations.screens.tabs.moreTab.documentation.DocumentationScreen
import com.bytheour.presentations.screens.tabs.moreTab.documentation.ImageFullScreen
import com.bytheour.presentations.screens.tabs.moreTab.inviteFriendsScreen.JtInviteFriendsScreen
import com.bytheour.presentations.screens.tabs.moreTab.mediationGuideline.JtMediationGuidelinesScreen
import com.bytheour.presentations.screens.tabs.moreTab.notification.JTNotificationScreen
import com.bytheour.presentations.screens.tabs.moreTab.profile.JTProfileEditScreen
import com.bytheour.presentations.screens.tabs.moreTab.reportAnIssue.JTReportAnIssueScreen
import com.bytheour.presentations.screens.tabs.moreTab.reviewAndRatings.ReviewAndRatingsScreen
import com.bytheour.presentations.screens.tabs.moreTab.termsAndConditionsScreen.JtTermsAndConditionsScreen
import com.bytheour.presentations.screens.tabs.moreTab.transactionHistory.TransactionHiStoryScreen
import com.bytheour.presentations.screens.tabs.moreTab.wallet.WalletScreen
import com.bytheour.presentations.screens.tabs.reports.AdminEditServiceRequestScreen
import com.bytheour.presentations.screens.tabs.reports.AdminUsersScreen
import com.bytheour.presentations.screens.tabs.reports.JTAdminReportsScreen
import com.bytheour.presentations.screens.tabs.reports.JTServiceRequestListScreen
import com.bytheour.presentations.screens.tabs.reports.JTServicesScreen
import com.bytheour.presentations.screens.tabs.reports.JtAdminDownloadsScreen
import com.bytheour.presentations.screens.tabs.reports.UserEditScreen
import com.bytheour.presentations.screens.tabs.reports.services.AdminManageServiceScreen
import com.bytheour.presentations.screens.tabs.reports.services.ManageServiceEditScreen
import com.bytheour.presentations.screens.tabs.reports.workers.AdminWorkersScreen
import com.bytheour.presentations.screens.tabs.reports.workers.EditWorkerDetailsScreen
import com.bytheour.presentations.screens.tabs.servicesTab.AddServiceScreen
import com.bytheour.presentations.screens.tabs.servicesTab.ImageUploadScreen
import com.bytheour.presentations.screens.tabs.servicesTab.SelectCategoryScreen
import com.bytheour.presentations.screens.tabs.servicesTab.SelectPlanScreen
import com.bytheour.presentations.screens.tabs.servicesTab.SelectServicesScreen
import com.bytheour.presentations.screens.tabs.servicesTab.ServiceRequestScreen
import com.bytheour.presentations.screens.tabs.servicesTab.ServicesTabScreen
import com.bytheour.presentations.screens.wellComeScreens.JTWelcomeScreens
import com.bytheour.utils.JTWebViewScreen
import com.bytheour.utils.PlatformActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

var navigateToLoginScreen: ((String) -> Unit)? = null

@Composable
fun JTBtoNavGraph(
    type:String,
    mainActivity: PlatformActivityContext?,
    navController : NavHostController = rememberNavController(),
    startDestination : JTBtoNavScreens = JTBtoNavScreens.Splash,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navActions : JTBtoNavActions = remember(navController) {
        JTBtoNavActions(navController)
    },
) {

    navigateToLoginScreen = { message ->
        coroutineScope.launch {
            navActions.navigateToUserTypeFromMore()
        }
    }

    SideEffect {
        if (type.isNotEmpty()) {
            when (type) {
                "booking-accepted",
                "quotation-accepted",
                "booking-payment-done",
                "quotation-sent",
                "booking-start",
                "booking-end",-> navActions.navigateToMainScreen(true, 0,false)
                "chat-message"-> navActions.navigateToMainScreen(false,0,true)
                else -> navActions.navigateToMainScreen(false,0,false)
            }
        }
    }

    NavHost(
        navController = navController,
        modifier = Modifier,
        startDestination = startDestination,
    ){
        composable<JTBtoNavScreens.Login> {
            JTLoginScreen(
                navigateToOtpScreen = {mobile->
                    navActions.navigateToOtp(mobile,null,"","","","","")
                },
                navigateToBack = {
                    navActions.navigateToUserType()
                },
                navigateToSignupScreen = {mobile ->
                    navActions.navigateToSignUp(mobile)
                },
                activityInstance = mainActivity,
            )
        }

        composable<JTBtoNavScreens.Otp> {
            val args =it.toRoute<JTBtoNavScreens.Otp>()
            JTOtpScreen(
                value = args.value?:0,
                mobile = args.mobile,
                firstName = args.firstName?:"",
                lastName = args.lastName?:"",
                userName = args.userName?:"",
                email = args.email?:"",
                profile = args.profile?:"",
                navigateToBack = {
                    navActions.navigateToLogin(isProvider = false)
                },
                navigateToLocation = {it->
                    navActions.navigateToLocation(it)
                },
                activityInstance = mainActivity,
            )
        }

        composable<JTBtoNavScreens.AskLocationPermission> {
            JtLocationEnableScreen(
                navigateToUserTypeScreen = {
                    navActions.navigateToUserType()
                }
            )
        }

        composable<JTBtoNavScreens.SignUp> {
            val args =it.toRoute<JTBtoNavScreens.SignUp>()
            JTSignUpScreen(
                mobile = args.mobile,
                from = args.value,
                navigateToOtp = {mobile,int,firstName,lastName,userName,email,profile ->
                    navActions.navigateToOtp(mobile,int,firstName,lastName,userName,email,profile)
                },
                navigateBack = {
                    navActions.navigateBack()
                },
                activityInstance = mainActivity,
            )
        }

        composable<JTBtoNavScreens.WellCome> {
            JTWelcomeScreens(
                navigateToLocationEnableScreen = {
                    navActions.navigateToAskLocationPermission()
                }
            )
        }

        composable<JTBtoNavScreens.WebViewScreen> {
            val args =it.toRoute<JTBtoNavScreens.WebViewScreen>()

            JTWebViewScreen(
                title = args.title,
                url = args.webUrl,
                showDownBox = false,
                onBackPress = {
                    navActions.navigateBack()
                },
                showSkipButton = true,
                onSkipClick = {
                    navActions.navigateSelectCategoryClear()
                }
            )
        }

        composable<JTBtoNavScreens.JtMainScreen> {
            val args =it.toRoute<JTBtoNavScreens.JtMainScreen>()

            JTBtoMainScreen(
                navigateToNotificationScreen = {
                    navActions.navigateToNotification()
                },
                navigateToProfileEditScreen = {
                    navActions.navigateToProfileEditScreen("")
                },
                navigateToOneOnOneChat = { name, roomId, isArchive,profile,fromId  ->
                    navActions.navigateToOneOnOneChat(name, roomId, isArchive,"",profile,fromId)
                },
                navigateToPrivacyAndPolicyScreen = {
                    navActions.navigateToPrivacyAndPolicy()
                },
                navigateToMediationGuidelines = {
                    navActions.navigateToMediationGuidelines()
                },
                navigateAddress = {it->
                    navActions.navigateAddresses(it)
                },
                navigateToReportAnIssueScreen = {
                    navActions.navigateToReportAnIssueScreen()
                },
                navigateToTermsAndConditionsScreen = {
                    navActions.navigateToTermsAndConditionsScreen()
                },
                navigateToAboutBtoScreen = {
                    navActions.navigateToAboutBTOScreen()
                },
                navigateToFaqScreen = {
                    navActions.navigateToFaq()
                },
                navigateToInviteFriendsScreen = {
                    navActions.navigateToInviteFriendsScreen()
                },
                navigateToUserScreen = {
                    navActions.navigateToAdminUserScreen()
                },
                navigateToWorkersScreen = {
                    navActions.navigateToWorkersScreen()
                },
                navigateToArchiveChat = {
                    navActions.navigateToArchivedChat()
                },
                navigateToUserTypeScreen = {
                    navActions.navigateToUserTypeFromMore()
                },
                navigateToBookingInfoScreen = {data,id ->
                    navActions.navigateToBookingInfoScreen(data,id)
                },
                navigateToDeleteScreen = {
                    navActions.navigateToDeleteScreen()
                },
                navigateProviderCancelReasonScreen = {it->
                    navActions.navigateToProviderCancelReasonScreen(it)
                },
                navigateToSendEstimationScreen = {id,time,cost ->
                    navActions.navigateToSendEstimateScreen(id,time,cost)
                },
                navigateToProviderBookingInfoScreen = {data,id->
                    navActions.navigateToProviderBookingInfoScreen(data,id)
                },
                navigateToServiceRequestScreen = {
                    navActions.navigateToServiceRequest()
                },
                navigateToManageServiceScreen = {
                    navActions.navigateToManageServiceRequest()
                },
                navigateToCategoryScreen = {
                    navActions.navigateSelectCategory()
                },
                navigatetoAddServiceScreen = { navigationValue,serviceId,serviceName,serviceDescription,image,hourlyRate,minHour,experience,description,isPremiseAvailable,catId,radius,iD ->
                    navActions.navigateAddServiceScreen(
                        navigationValue =navigationValue ,
                        serviceId = serviceId,
                        serviceName = serviceName,
                        serviceDescription = serviceDescription,
                        image = image,
                        hourlyRate = hourlyRate,
                        minHour = minHour,
                        experience = experience,
                        description = description,
                        isPremiseAvailable = isPremiseAvailable,
                        catId = catId,
                        radius = radius,
                        iD = iD
                    )
                },
                navigateToWalletScreen = {
                    navActions.navigateWalletScreen()
                },
                navigateToTransactionHistoryScreen = {
                    navActions.navigateTransactionHistoryScreen()
                },
                navigateToReviewAndRatingsScreen = {
                    navActions.navigateToReviewAndRatingsScreen()
                },
                navigateToChatSupportScreen = {
                    navActions.navigateToChatSupportScreen()
                },
                navigateToNotificationListScreen = {
                    navActions.navigateToNotificationListScreen()
                },
                fromBookings = args.fromBookings,
                fromServices = args.fromServices?:0,
                navigateToMainScreen = {
                    navActions.navigateToMainScreen(false,0,false)
                },
                navigateToCustomerCategoryScreen = {
                    navActions.navigateToCustomerCategory()
                },
                navigateToRentalsScreen = {id ,title ->
                    navActions.navigateToCustomerRentals(id,title)
                },
                navigateToAvailableServicesScreen = {id ,title ->
                    navActions.navigateToAvailableServicesScreen(id,title)
                },
                navigateToDocumentsScreen = {
                    navActions.navigateToDocumentsScreen()
                },
                navigateToBankDetailsScreen = {
                    navActions.navigateToBankDetailsScreen()
                },
                navigateSignUpScreen = {mbl,frm ->
                    navActions.navigateToSignUpcClear(mbl,frm)
                },
                navigateToProfileEditScreenFromDialog = {it->
                    navActions.navigateToProfileEditScreen(it)
                },
                navController = navController,
                navigateToSelectLocation = {lat,lang,serv ->
                    navActions.navigateSelectLocation(lat,lang,serv)
                },
                navigateToWebScreen = { webUrl, title ->
                    navActions.navigateToWebViewScreen(webUrl,title)
                },
                fromMessage = args.fromMessage,
                navigateToInspectionReportScreen = {providerName,title,image,id ->
                    navActions.navigateToInspectionReport(providerName,title,image,id)
                }
            )
        }


        composable<JTBtoNavScreens.UserType> {
            JTUserTypeScreen(
                navigateToLoginScreen = {isProvider->
                    navActions.navigateToLogin(isProvider)
                }
            )
        }
        composable<JTBtoNavScreens.ProfileEditScreen> {
            val args =it.toRoute<JTBtoNavScreens.ProfileEditScreen>()
            JTProfileEditScreen(
                onBackClick = {
                    navActions.navigateBack()
                },
                navigateToUserTypeScreen = {
                    navActions.navigateToUserTypeFromMore()
                },
                navigateToDeleteScreen = {
                    navActions.navigateToDeleteScreen()
                },
                fromDialog = args.fromDialog,
            )
        }

        composable<JTBtoNavScreens.MoreTabScreen> {
            JTMoreTabScreen(
                navigateToProfileEditScreen = {
                    navActions.navigateToProfileEditScreen("")
                },
                navigateToMediationGuidelines = {
                    navActions.navigateToMediationGuidelines()
                },
                navigateToPrivacyAndPolicyScreen = {
                    navActions.navigateToPrivacyAndPolicy()
                },
                navigateAddress = {
                    navActions.navigateAddresses(it)
                },
                navigateToTermsAndConditionsScreen = {
                    navActions.navigateToTermsAndConditionsScreen()
                },
                navigateToAboutBtoScreen = {
                    navActions.navigateToAboutBTOScreen()
                },
                navigateToFaqScreen = {
                    navActions.navigateToFaq()
                },
                navigateToReportAnIssueScreen = {
                    navActions.navigateToReportAnIssueScreen()
                },
                navigateToInviteFriendsScreen = {
                    navActions.navigateToInviteFriendsScreen()
                },
                navigateToUserTypeScreen = {
                    navActions.navigateToUserTypeFromMore()
                },
                navigateToDeleteScreen ={
                    navActions.navigateToDeleteScreen()
                },
                navigateToWalletScreen = {
                    navActions.navigateWalletScreen()
                },
                navigateToTransactionHistoryScreen = {
                    navActions.navigateTransactionHistoryScreen()
                },
                navigateToReviewAndRatingsScreen = {
                    navActions.navigateToReviewAndRatingsScreen()
                },
                navigateToChatSupportScreen = {
                    navActions.navigateToChatSupportScreen()
                },
                navigateToMainScreen = {
                    navActions.navigateToMainScreen(false,0,false)
                },
                navigateToDocumentsScreen = {
                    navActions.navigateToDocumentsScreen()
                },
                navigateToBankDetailsScreen = {
                    navActions.navigateToBankDetailsScreen()
                },
                navigateToSignUpScreen = {mbl,frm ->
                    navActions.navigateToSignUpcClear(mbl,frm)
                },
                navigateToProfileEditScreenFromDialog = {
                    navActions.navigateToProfileEditScreen(it)
                },
//                navController = navController
            )
        }

        composable<JTBtoNavScreens.Location> {
            JTLocationFetchingScreen(
                navigateToMainScreen = {
                    navActions.navigateToMainScreen(false,0,false)
                },
            )
        }

        composable<JTBtoNavScreens.Splash> {
            JTSplashScreen(
                navigateToWellComeScreen = {
                    navActions.navigateToWellCome()
                },
                navigateToLocationScreen = {
                    navActions.navigateToLocation()
                }
            )
        }
        composable<JTBtoNavScreens.ServicesTabScreen> {
            ServicesTabScreen(
                navigateToCategoryScreen = {
                    navActions.navigateSelectCategory()
                },
                navigateToSelectLocation = { lat,long,service ->
                    navActions.navigateSelectLocation(lat,long,service)
                },
                navigateToAddServiceScreen = { navigationValue, serviceId, serviceName, serviceDescription, image, hourlyRate, minHour, experience, description, isPremiseAvailable, catId, radius, iD:String ->
                    navActions.navigateAddServiceScreen(
                        navigationValue =navigationValue ,
                        serviceId = serviceId,
                        serviceName = serviceName,
                        serviceDescription = serviceDescription,
                        image = image,
                        hourlyRate = hourlyRate,
                        minHour = minHour,
                        experience = experience,
                        description = description,
                        isPremiseAvailable = isPremiseAvailable,
                        catId = catId,
                        radius = radius,
                        iD = iD

                    )
                },
                navigateToWebView = { webUrl, title ->
                    navActions.navigateToWebViewScreen(webUrl,title)
                }
            )
        }

        composable<JTBtoNavScreens.SelectCategory> {
            SelectCategoryScreen(
                navigateToServicesScreen = { id ,title ->
                    navActions.navigateSelectService(id,title)
                },
                navigateBack = {
                    navActions.navigateBack()
                },

            )
        }


        composable<JTBtoNavScreens.SelectService> {
            val args = it.toRoute<JTBtoNavScreens.SelectService>()
            SelectServicesScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToAddServiceScreen = {navigationValue,serviceId,serviceName,serviceDescription,image,catId,iD ->
                    navActions.navigateAddServiceScreen(
                        navigationValue =navigationValue ,
                        serviceId = serviceId,
                        serviceName = serviceName,
                        serviceDescription = serviceDescription,
                        image = image,
                        hourlyRate = "",
                        minHour = "",
                        experience = "",
                        description = "",
                        isPremiseAvailable = "",
                        catId = catId,
                        radius = "",
                        iD = iD

                    )
                },
               id = args.id,
                title = args.title,
            )
        }

        composable<JTBtoNavScreens.AddService> {
            val args = it.toRoute<JTBtoNavScreens.AddService>()
            AddServiceScreen(
//                navController = navController,
                navigateBack = {
                    navActions.navigateBack()
                },
                serviceId = args.serviceId,
                serviceTitle = args.serviceTitle,
//                serviceDescription = args.serviceDescription,
                navigationValue = args.navigationValue,
                image = args.image,
                hourlyRate = args.hourlyRate,
                minHours = args.minHours,
                experience = args.experience,
                description = args.description,
                isPremiseAvailable = args.isPremiseAvailable,
//                catId = args.catId,
                radius = args.radius,
                id = args.id,
//                navigateToServiceRequestScreen = { catId ->
//                    navActions.navigateToServiceRequestScreen(catId)
//                },
                navigatingtomainScreen = {
                navActions.navigateToMainScreen(false,it,false)
                },
                navigateImageUploadScreen = {id,value,plan ->
                    navActions.navigateToImageUploadScreen(id,value,plan)
                },
                navigateSelectPlanScreen = {
                    navActions.navigateToSelectPlanScreen(it)
                }
            )
        }


        composable<JTBtoNavScreens.ExploreTabScreen> {
            JtExploreTabScreen(
                navigateToAddressScreen = {
                    navActions.navigateAddresses(it)
                },
                navigateToNotificationListScreen = {
                    navActions.navigateToNotificationListScreen()
                },
                navigateToCategoryScreen = {
                    navActions.navigateToCustomerCategory()
                },
                navigateToRentalsScreen = {id ,title ->
                    navActions.navigateToCustomerRentals(id ,title)
                },
                navigateToAvailableServices = {id ,title ->
                    navActions.navigateToAvailableServicesScreen(id,title)
                }
            )
        }

        composable<JTBtoNavScreens.BookingsTabScreen> {
            JtBookingsTabScreen(
                navigateToOneOnOneChat = {name,roomId ,isArchive,profile,fromId->
                    navActions.navigateToOneOnOneChat(name,roomId ,isArchive,"",profile,fromId)
                },
                navigateToBookingInfoScreen = {data,id ->
                    navActions.navigateToBookingInfoScreen(data,id)
                },
            )
        }
        composable<JTBtoNavScreens.ProviderBookingScreen> {
            JtProviderBookingsTabScreen(
                navigateToOneOnOneChat = {name,roomId ,isArchive,profile,fromId->
                    navActions.navigateToOneOnOneChat(name,roomId ,isArchive,"",profile,fromId)
                },
                navigateToProviderBookingInfoScreen = {data,id ->
                    navActions.navigateToProviderBookingInfoScreen(data,id)
                },
                navigateProviderCancelReasonScreen = {
                    navActions.navigateToProviderCancelReasonScreen(it)
                },
                navigateToSendEstimationScreen = {id,time,rate ->
                    navActions.navigateToSendEstimateScreen(id,time,rate)
                },
                navigateToInspectionReportScreen = {providerName,title,image,id ->
                    navActions.navigateToInspectionReport(providerName,title,image,id)
                }

            )
        }

        composable<JTBtoNavScreens.ProviderBookingInfo> {
            val argsId = it.toRoute<JTBtoNavScreens.ProviderBookingInfo>()
            ProviderBookingInfoScreen(
                navigateToBookingTabScreen = {
                    navActions.navigateBack()
                },
                navigateToOneOnOneChat = { name, id, isArchive,profile,fromId ->
                    navActions.navigateToOneOnOneChat(name, id, isArchive,"",profile,fromId)
                },
                navigateProviderCancelReasonScreen = {it->
                    navActions.navigateToProviderCancelReasonScreen(it)
                },
                navigateToSendEstimationScreen = {id,time,cost->
                    navActions.navigateToSendEstimateScreen(id,time,cost)
                },
//                bookingData1 = bookingData,
                navigateToMediationScreen = { id, amount, total ->
                    navActions.navigateToMediationRefundScreen(id,amount,total)
                },
                navigateToInspectionReportScreen = {providerName,title,image,id ->
                    navActions.navigateToInspectionReport(providerName,title,image,id)
                },
                bookingId =argsId.bookingId
            )
        }
        composable<JTBtoNavScreens.MediationRefundScreens> {
            val args =it.toRoute<JTBtoNavScreens.MediationRefundScreens>()
            MediationRefundingScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
               bookingId = args.bookingId,
                amount = args.amount,
                total = args.total
            )
        }

        composable<JTBtoNavScreens.ProviderCancelReasonScreen> {
            val args =it.toRoute<JTBtoNavScreens.ProviderCancelReasonScreen>()
            CancelBookingReasonsScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                id = args.id
            )
        }

        composable<JTBtoNavScreens.SendEstimateScreen> {
            val args =it.toRoute<JTBtoNavScreens.SendEstimateScreen>()
            SendEstimateScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                bookingId = args.id,
                time = args.time,
                cost = args.rate
            )
        }
        composable<JTBtoNavScreens.NotificationScreen> {
            JTNotificationScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.OneOnOneChat> {
            val args =it.toRoute<JTBtoNavScreens.OneOnOneChat>()
            OneOnOneChatScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToAttachment = {it->
                    navActions.navigateToAttachment(it)
                },
                name = args.name,
                roomId = args.roomId,
                isArchive = args.isArchive,
                profile = args.profile?:"",
                fromChatSupportId = args.fromChatSupport?:"",
                fromID = args.fromID?:""
            )
        }

        composable<JTBtoNavScreens.Gallery> {
            JTGalleryAndCamera(
                navigateToPermissions ={
                    navActions.navigatePermissions()
                }
            )
        }

        composable<JTBtoNavScreens.AskPermission> {
            JTAskPermission()
        }

        composable<JTBtoNavScreens.Address> {
            val args =it.toRoute<JTBtoNavScreens.Address>()
            JTAddressList(
                navigateToSelectLocation = {lat, long->
                    navActions.navigateSelectLocation(lat,long,"")
                },
                fromHomeScreen = args.fromHome,
                navigateToMoreTab = {
                    navActions.navigateBack()
                },
                navigateToTrendingServices = {
                    navActions.navigateToTrendingServicesScreen()
                }
            )
        }
        composable<JTBtoNavScreens.ReportAnIssue> {
            JTReportAnIssueScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.MediationGuidelines> {
            JtMediationGuidelinesScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }


        composable<JTBtoNavScreens.PrivacyAndPolicy> {
            PrivacyAndPolicyScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.Faq> {
            FAQScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }


        composable<JTBtoNavScreens.TermsAndConditions> {
            JtTermsAndConditionsScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.AboutScreen> {
            JtAboutScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }
        composable<JTBtoNavScreens.DeleteScreen> {
            DeleteScreen(
                onBackClick = {
                    navActions.navigateBack()
                },
                navigateToUserTypeScreen = {
                    navActions.navigateToUserType()
                }
            )
        }

        composable<JTBtoNavScreens.InviteFriend> {
            JtInviteFriendsScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.AdminUsers> {
            AdminUsersScreen(
                navigateUserEditScreen = {id,firstName,lastName,email,phoneNumber,image->
                    navActions.navigateToUsersEditScreen(id,firstName,lastName,email,phoneNumber,image)
                },
                navigateBack = {navActions.navigateBack()}
            )
        }

        composable<JTBtoNavScreens.UsersEdit> {
            val args =it.toRoute<JTBtoNavScreens.UsersEdit>()
            UserEditScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                id = args.id,
                firstName = args.firstName?:"",
                lastName = args.lastName?:"",
                email = args.email?:"",
                phoneNumber = args.contactNumber?:"",
                image = args.image?:""

            )
        }


        composable<JTBtoNavScreens.AdminReportsScreen> {
            JTAdminReportsScreen(
                navigateToWorkersScreen = {
                    navActions.navigateToWorkersScreen()
                },
                navigateToUserScreen = {
                    navActions.navigateToAdminUserScreen()
                },
                navigateToServiceRequestScreen = {
                    navActions.navigateToServiceRequest()
                },
                navigateToManageServiceScreen = {
                    navActions.navigateToManageServiceRequest()
                },

            )
        }

        composable<JTBtoNavScreens.ServiceScreen> {
            JTServicesScreen(
                navigateToServiceRequestScreen = {
                    navActions.navigateToServiceRequest()
                },
                navigateToManageServiceScreen = {
                    navActions.navigateToManageServiceRequest()
                },
                navigateBack = {
                    navActions.navigateBack()
                },
            )
        }

        composable<JTBtoNavScreens.ServiceRequest> {
            JTServiceRequestListScreen(
                navigateToServiceEditScreen = {id:Int,title,description,category,avatar,sequence,serviceStatus->
                    navActions.navigateToServiceEditScreen(
                        id = id,
                        title = title,
                        description = description,
                        category = category,
                        avatar = avatar,
                        sequence = sequence,
                        serviceStatus
                    )
                },
                navigateBack = {
                    navActions.navigateBack()
                }
            )
        }
        composable<JTBtoNavScreens.AdminDownloadsScreen> {
            JtAdminDownloadsScreen(
                onBackClick = {
                    navActions.navigateBack()
                }
            )
        }
        composable<JTBtoNavScreens.AdminEditServiceRequestScreen> {
            val args =it.toRoute<JTBtoNavScreens.AdminEditServiceRequestScreen>()
            AdminEditServiceRequestScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                id = args.id,
                title = args.title,
                description = args.description,
                category = args.category,
                avatar = args.avatar,
                sequence = args.sequence,
                serviceStatus = args.serviceStatus

            )
        }

        composable<JTBtoNavScreens.AdminEditManageServiceRequestScreen> {
            val args =it.toRoute<JTBtoNavScreens.AdminEditManageServiceRequestScreen>()
            ManageServiceEditScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                id = args.id,
                title = args.title,
                description = args.description,
                category = args.category,
                avatar = args.avatar,
                sequence = args.sequence,
                serviceStatus = args.serviceStatus,
                categoryId = args.categoryId

            )
        }

        composable<JTBtoNavScreens.AdminManageServiceScreen> {
            AdminManageServiceScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToManageServiceEditScreen = {id:Int,title,description,category,avatar,sequence,serviceStatus,categoryId->
                    navActions.navigateToManageServiceEditScreen(
                        id = id,
                        title = title,
                        description = description,
                        category = category,
                        avatar = avatar,
                        sequence = sequence,
                        serviceStatus,
                        categoryId
                    )
                },
            )
        }

        composable<JTBtoNavScreens.AdminWorkersScreen> {
            AdminWorkersScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateWorkerEditScreen = {userId,firstName,lastName,email,phoneNumber,profileImage,accountType,documentStatus ->
                    navActions.navigateToEditWorkersScreen(
                        id = userId,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        contactNumber = phoneNumber,
                        profileImage = profileImage,
                        accountType = accountType,
                        documentStatus = documentStatus
                    )
                }
            )
        }

        composable<JTBtoNavScreens.AdminWorkersEditScreen> {
            val args =it.toRoute<JTBtoNavScreens.AdminWorkersEditScreen>()
            EditWorkerDetailsScreen(
                userId = args.userid,
                firstname = args.firstName,
                lastname = args.lastName,
                email = args.email,
                phoneNumber = args.contactNumber,
                profileImage = args.profileImage,
                accountType = args.accountType,
                documentStatus = args.documentStatus,
                navigateBack = {
                    navActions.navigateBack()
                },
            )
        }


        composable<JTBtoNavScreens.ArchivedChat> {
            ArchivedChatScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToOneOnOneChat = {name , roomId ,isArchive,profile,fromId ->
                    navActions.navigateToOneOnOneChat(name , roomId,isArchive,"",profile,fromId)
                }
            )
        }

        composable<JTBtoNavScreens.Attachment> {
            val args =it.toRoute<JTBtoNavScreens.Attachment>()
            ChatAttachmentDisplayScreen(
                image = args.message,
                navigateBack = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.GooglePlaces> {
            val args =it.toRoute<JTBtoNavScreens.GooglePlaces>()
            JTGooglePlaceSearch(
                navigateBack = {
                    navActions.navigateBackClear()
                               }
                ,
                searchText = args.searchText
            )
        }

        composable<JTBtoNavScreens.SelectLocation> {
            val args = it.toRoute<JTBtoNavScreens.SelectLocation>()
            JTSelectLocation(
                navigateToGoogleSearch = {searchText->
                    navActions.navigateToGooglePlacesScreen(searchText =searchText)
                },
                navigateBack = {navActions.navigateBack()},
                longitude = args.long,
                latitude = args.lat,
                fromService = args.value,
                navigateToWebView = { webUrl, title ->
                    navActions.navigateToWebViewScreenClear(webUrl,title)
                }
            )
        }


        composable<JTBtoNavScreens.BookingInfo> {
            val args = it.toRoute<JTBtoNavScreens.BookingInfo>()

            BookingInfoScreen(
                navigateToBookingTabScreen = {
                    navActions.navigateBack()
                },
                navigateToOneOnOneChat = { name, id, isArchive ,profile,fromId->
                    navActions.navigateToOneOnOneChat(name, id, isArchive,"",profile,fromId)
                },
                navigateToCancelBookingScreen = { id ,cancelAfterStartTime,providerStartTime,date ->
                    navActions.navigateToCancelBookingScreen(id,cancelAfterStartTime,providerStartTime,date)
                },
                navigateToMediationScreen = {it->
                    navActions.navigateToMediationScreen(it)
                },
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToViewProfileScreen = {it->
                    navActions.navigateToViewProfileScreen(it)
                },
//                bookingData1 = bookingData,
                navigateToChatSupportScreen = {it->
                    navActions.navigateToChatSupport(it)
                },
                navigateToBillingSummaryScreen = {id,fromInspection->
                    navActions.navigateToBillingSummary(id,fromInspection)
                },
                bookingId = args.bookingId,
                navigateToViewInspectionDetails = {title , image ,name ,id ->
                    navActions.navigateToInspectionProceedingJob(title,image,name,id)
                }
            )
        }
        composable<JTBtoNavScreens.TrendingServices> {
            TrendingServices(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToServicesScreen = {id,title->
                    navActions.navigateToAvailableServicesScreen(id,title)
                },
                navigateToSelectionScreen = {id,title,description,image,fromInspection,inspectionRate->
                    navActions.navigateToDateSelectionScreen(serviceId = id, title = title, description = description, image = image, fromInspection = fromInspection, inspectionRate = inspectionRate)
                },
            )
        }

        composable<JTBtoNavScreens.AvailableServices> {
            val args =it.toRoute<JTBtoNavScreens.AvailableServices>()
            AvailableServices(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToSelectionScreen = {id,title,description,image,fromInspection,inspectionRate->
                    navActions.navigateToDateSelectionScreen(serviceId = id, title = title, description = description, image = image, fromInspection = fromInspection, inspectionRate = inspectionRate)
                },
                id = args.id,
                title = args.title,
            )
        }

        composable<JTBtoNavScreens.PaymentScreen> {
            val args = it.toRoute<JTBtoNavScreens.PaymentScreen>()
            PaymentScreen(
                navigateToAddNewCard = {
                    navActions.navigateAddNewCard()
                },
                navigateBack = {
                    navActions.navigateBack()
                },
                bookingId = args.bookingId
            )
        }


        composable<JTBtoNavScreens.AddCardDetails> {
            AddCardDetailsScreen(
                navigateToCardList = {
                    navActions.navigateToCardList("")
                }
            )
        }

        composable<JTBtoNavScreens.CancelBooking> {
            val args =it.toRoute<JTBtoNavScreens.CancelBooking>()
            CancelBookingReasonsScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                id = args.id,
                cancelAfterStartTime = args.cancelAfterStartTime,
                providerStartTime = args.providerStartTime,
                date = args.date
            )
        }

        composable<JTBtoNavScreens.MediationScreen> {
            val args =it.toRoute<JTBtoNavScreens.MediationScreen>()
            RequestMediationScreen(
                bookingId = args.id,
                navigateBack = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.SelectDateOfService> {
            val args =it.toRoute<JTBtoNavScreens.SelectDateOfService>()
            SelectionDateOfService(
                navigateToConfirmationScreen = {title,address,image,bookingId->
                    navActions.navigateToBookingConfirmationScreen(title = title, address = address, image = image, bookingId = bookingId)
                },
                navigateBack = {
                    navActions.navigateBack()
                },
                serviceId = args.serviceId,
                title = args.title,
                description = args.description,
                imageUrl = args.image,
                navigateToInspectionBilling = {title,address,image,bookingId->
                    navActions.navigateToInspectionBillingDetails(title,address,image,bookingId)
                },
                fromInspection = args.fromInspection,
                inspectionBookingId = args.inspectionBookingId?:"",
                inspectionRate = args.inspectionRate?:""
            )
        }
        composable<JTBtoNavScreens.BookingRequestSentScreen> {
            val args =it.toRoute<JTBtoNavScreens.BookingRequestSentScreen>()
            BookingConfirmationScreen(
                address = args.address,
                serviceName = args.title,
                imageUrl = args.image,
                bookingId = args.bookingId,
                navigateToMainScreen = {it->
                    navActions.navigateToMainScreen(it,0,false)
                },
            )
        }

        composable<JTBtoNavScreens.ServiceRequestScreen> {
            val args =it.toRoute<JTBtoNavScreens.ServiceRequestScreen>()
            ServiceRequestScreen(
                onBackClick = {
                    navActions.navigateBack()
                },
                catId = args.catId
            )
        }


        composable<JTBtoNavScreens.TransactionHistoryScreen> {
            TransactionHiStoryScreen(
                onBackClick = { navActions.navigateBack() },

            )
        }

        composable<JTBtoNavScreens.WalletScreen> {
            WalletScreen(
                onBackClick = {
                    navActions.navigateBack()
                },
//                onTransactionClick = {
//                    navActions.navigateToTransactionDetailScreen(it)
//                }
            )
        }


        composable<JTBtoNavScreens.ReviewAndRatingsScreen> {
            ReviewAndRatingsScreen(
                onBackPress = {
                    navActions.navigateBack()
                }
            )
        }
        composable<JTBtoNavScreens.ChatSupportScreen> {
            ChatSupportScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToViewAllTickets = {
                    navActions.navigateToAllTicketsScreen()

                },
                navigateDetailsScreen = {
                    navActions.navigateToDetailsScreen(it)
                },
                navigateToChatScreen = {id ->
                    navActions.navigateToOneOnOneChat(
                        name = "",
                        roomId = "",
                        isArchive = false,
                        supportId = id,
                        profile = "",
                        fromID = ""
                    )
                }
            )
        }

        composable<JTBtoNavScreens.AllTicketsScreen> {
            AllTicketsScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateDetailsScreen = {
                    navActions.navigateToDetailsScreen(it)
                },
                navigateToChatScreen = {id ->
                    navActions.navigateToOneOnOneChat(
                        name = "",
                        roomId = "",
                        isArchive = false,
                        supportId = id,
                        profile = "",
                        fromID = ""
                    )
                }
            )
        }

        composable<JTBtoNavScreens.DetailsScreen> {
            val args =it.toRoute<JTBtoNavScreens.DetailsScreen>()
            DetailsScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                bookingId = args.bookingId,
                navigateToChatScreen = {id->
                    navActions.navigateToOneOnOneChat(
                        name = "",
                        roomId = "",
                        isArchive = false,
                        supportId = id,
                        profile = "",
                        fromID = ""
                    )
                }

            )
        }

        composable<JTBtoNavScreens.ViewProfile> {
            val args =it.toRoute<JTBtoNavScreens.ViewProfile>()
            val bookingData = Json.decodeFromString<BookingData>(args.bookingDataJson)
            ViewProviderProfileScreen(
                navigateBack = {navActions.navigateBack()},
                bookingData = bookingData,
                navigateToChatScreen = {
                    navActions.navigateToOneOnOneChat(bookingData.workerName, "", false,"",bookingData.providerImage ?: "",bookingData.workerId)
                }
            )
        }

        composable<JTBtoNavScreens.ChatSupport> {
            val args =it.toRoute<JTBtoNavScreens.ChatSupport>()
            ChatSupportScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                bookingId = args.id
            )
        }

        composable<JTBtoNavScreens.NotificationListScreen> {
            NotificationListScreen(
                navigateBack = {
                    navActions.navigateBack()
                }
            )
        }
        composable<JTBtoNavScreens.ImageUploadScreen> {
            val args =it.toRoute<JTBtoNavScreens.ImageUploadScreen>()
            ImageUploadScreen(
                onBackClick = {
                    navActions.navigateBack()
                },
                serviceId = args.id,
                value = args.value,
                navigateSelectPlanScreen = {
                    navActions.navigateToSelectPlanScreen("")
                }
            )
        }

        composable<JTBtoNavScreens.CustomerCategory> {
            CustomerCategoriesScreen(
                navigateToAvailabilityScreen = { id, title ->
                    navActions.navigateToAvailableServicesScreen(id,title)
                },
                navigateBack = {
                    navActions.navigateBack()
                }
            )
        }

        composable<JTBtoNavScreens.CustomerRentals> {
            val args =it.toRoute<JTBtoNavScreens.CustomerRentals>()
            CustomerRentalsScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                id = args.id,
                title = args.title
            )
        }

        composable<JTBtoNavScreens.Documents> {
            DocumentationScreen(
                onBackPress = {
                    navActions.navigateBack()
                },
                navigateToImageFullScreen = {image,desc ->
                    navActions.navigateToImageFullScreen(
                        image = image,
                        description = desc
                    )
                }
            )
        }

        composable<JTBtoNavScreens.ImageFullScreen> {
            val args =it.toRoute<JTBtoNavScreens.ImageFullScreen>()
          ImageFullScreen(
                onBackPress = {
                    navActions.navigateBack()
                },
              image = args.image,
              description = args.desc
            )
        }


        composable<JTBtoNavScreens.BankDetailsScreen> {
            BankDetailsScreen(
                onBackPress = {
                    navActions.navigateBack()

                }
            )
        }
        composable<JTBtoNavScreens.SelectPlanScreen> {
            val args =it.toRoute<JTBtoNavScreens.SelectPlanScreen>()
            SelectPlanScreen(
                serviceid = args.serviceId,
                onBackClick = {
                    navActions.navigateBack()
                },
                navigateToimageUploadScreen = {it->
                    navActions.navigateToImageUploadScreenClear(it)
                }
            )
        }
        composable<JTBtoNavScreens.BillingSummary> {
            val args =it.toRoute<JTBtoNavScreens.BillingSummary>()
            BillingSummaryScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                navigateToMainScreen = {
                    navActions.navigateToMainScreen(true,0,false)
                },
                bookingId = args.id,
                fromInspection = args.fromInspection
            )

        }

        composable<JTBtoNavScreens.InspectionReport> {
            val args =it.toRoute<JTBtoNavScreens.InspectionReport>()
            InspectionReportScreen(
                onBackClick = {
                    navActions.navigateBack()
                },
                providerName = args.providerName,
                serviceName = args.title,
                serviceImage = args.image,
                bookingId = args.bookingId,
                navigateToMainScreen = {
                    navActions.navigateToMainScreen(true,0,false)
                }
            )
        }


        composable<JTBtoNavScreens.InspectionRequestSentScreen> {
            val args =it.toRoute<JTBtoNavScreens.InspectionRequestSentScreen>()
            InspectionRequestSentScreen(
                serviceName = args.title,
                address = args.address,
                imageUrl = args.image,
                bookingId = args.bookingId,
                navigateToMainScreen = {
                    navActions.navigateToMainScreen(it,0,fromMessage = false)
                },
            )
        }


        composable<JTBtoNavScreens.InspectionProceedingJob> {
            val args =it.toRoute<JTBtoNavScreens.InspectionProceedingJob>()
            InspectionProceedingJob(
                onBack = {
                    navActions.navigateBack()
                },
                navigateToInspectionDecline = {id ->
                    navActions.navigateToInspectionDecline(id)
                },
                title = args.title,
                image = args.image ,
                name = args.name,
                bookingId = args.id,
                navigateToBillingSummaryScreen = { bookingId,fromInspection ->
                    navActions.navigateToBillingSummary(bookingId,fromInspection)
                }
            )
        }

        composable<JTBtoNavScreens.InspectionBillingScreen> {
            val args =it.toRoute<JTBtoNavScreens.InspectionBillingScreen>()
            InspectionBillingSummaryScreen(
                navigateBack = {
                    navActions.navigateBack()
                },
                bookingId = args.bookingId,
                address = args.address,
                title = args.title,
                image = args.image,
                navigateNotInspectionRequestSent = {address,title,image,bookingId ->
                    navActions.navigateToInspectionRequestSentScreen(address = address, title = title, image = image, bookingId = bookingId)
                }
            )
        }


        composable<JTBtoNavScreens.InspectionDecline>{
            val args =it.toRoute<JTBtoNavScreens.InspectionDecline>()
            InspectionDeclineScreen(
                onBackClick = {
                    navActions.navigateBack()
                },
                navigateToSelectionDateScreen = { serviceId, title, description, image ,fromInspection,inspectionBookingId->
                    navActions.navigateToDateSelectionScreen(
                        serviceId = serviceId,
                        title = title,
                        description = description,
                        image = image,
                        fromInspection = fromInspection,
                        inspectionBookingId = inspectionBookingId
                    )
                },
                bookingId = args.id,
                navigateToMainScreen = {
                    navActions.navigateToMainScreen(false,0,false)
                }
            )
        }
    }
}


