package com.bytheour.presentations.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.bookings
import bytheour.composeapp.generated.resources.bookings_active_ico
import bytheour.composeapp.generated.resources.bookings_ico
import bytheour.composeapp.generated.resources.explore
import bytheour.composeapp.generated.resources.explore_active_ico
import bytheour.composeapp.generated.resources.explore_ico
import bytheour.composeapp.generated.resources.messages
import bytheour.composeapp.generated.resources.messages_active_ico
import bytheour.composeapp.generated.resources.messages_ico
import bytheour.composeapp.generated.resources.more
import bytheour.composeapp.generated.resources.more_active_ico
import bytheour.composeapp.generated.resources.more_ico
import bytheour.composeapp.generated.resources.report
import bytheour.composeapp.generated.resources.reports_active_ico
import bytheour.composeapp.generated.resources.reports_ico
import bytheour.composeapp.generated.resources.services
import bytheour.composeapp.generated.resources.services_active_ico
import bytheour.composeapp.generated.resources.services_caps
import com.bytheour.presentations.screens.tabs.bookings.BookingData
import com.bytheour.presentations.screens.tabs.bookings.JtBookingsTabScreen
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.BookingDataProvider
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.JtProviderBookingsTabScreen
import com.bytheour.presentations.screens.tabs.explore.JtExploreTabScreen
import com.bytheour.presentations.screens.tabs.messages.JTChatListScreen
import com.bytheour.presentations.screens.tabs.moreTab.JTMoreTabScreen
import com.bytheour.presentations.screens.tabs.providerHomeTab.ProviderDashBoardScreen
import com.bytheour.presentations.screens.tabs.reports.JTAdminReportsScreen
import com.bytheour.presentations.screens.tabs.servicesTab.ServicesTabScreen
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.JTBottomNavigationBar
import com.bytheour.utils.NavItem
import com.bytheour.utils.TYPE_CUSTOMER
import com.bytheour.utils.TYPE_PROVIDER
import com.bytheour.utils.getKoinViewModel
import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.stringResource


@Composable
fun JTBtoMainScreen(
    jtBtoMainScreenViewModel: JTBtoMainScreenViewModel = getKoinViewModel(),
    navigateToNotificationScreen:()->Unit,
    navigateToProfileEditScreen:() -> Unit,
    navigateToOneOnOneChat: (String,String,Boolean,String?,String?) -> Unit,
    navigateToMediationGuidelines:()-> Unit,
    navigateToPrivacyAndPolicyScreen:() -> Unit,
    navigateAddress: (Boolean) ->Unit,
    navigateToTermsAndConditionsScreen:() -> Unit,
    navigateToAboutBtoScreen:()-> Unit,
    navigateToReportAnIssueScreen:() -> Unit,
    navigateToFaqScreen:() -> Unit,
    navigateToInviteFriendsScreen:() -> Unit,
    navigateToUserScreen:()-> Unit,
    navigateToArchiveChat:()-> Unit,
    navigateToWorkersScreen:() ->Unit,
    navigateProviderCancelReasonScreen:(String) ->Unit,
    navigateToSendEstimationScreen:(String,String,String) ->Unit,
    navigateToBookingInfoScreen: (BookingData,String) -> Unit,
    navigateToUserTypeScreen:() ->Unit,
    navigateToDeleteScreen:()->Unit,
    navigateToProviderBookingInfoScreen:(BookingDataProvider,String)->Unit,
    navigateToServiceRequestScreen:()->Unit,
    navigateToManageServiceScreen:() ->Unit,
    navigateToCategoryScreen:()-> Unit,
    navigatetoAddServiceScreen:(String,String,String,String,String,String,String,String,String,String,String,String,String)->Unit,
    navigateToWalletScreen:()->Unit,
    navigateToTransactionHistoryScreen:()->Unit,
    navigateToReviewAndRatingsScreen:()->Unit,
    navigateToChatSupportScreen:() ->Unit,
    fromServices:Int,
    navigateToNotificationListScreen:()->Unit,
    navigateToCustomerCategoryScreen:()->Unit,
    navigateToRentalsScreen:(String,String)-> Unit,
    navigateToAvailableServicesScreen:(String,String)-> Unit,
    fromBookings :Boolean,
    navigateToMainScreen:()-> Unit,
    navigateToDocumentsScreen:() ->Unit,
    navigateToBankDetailsScreen:() ->Unit,
    navigateSignUpScreen:(String,String)->Unit,
    navigateToProfileEditScreenFromDialog:(String) -> Unit,
    navigateToSelectLocation:(String,String,String)->Unit,
    navController: NavController,
    navigateToWebScreen:(String,String) -> Unit,
    fromMessage:Boolean,
    navigateToInspectionReportScreen:(String,String,String,String)->Unit
) {

    val selectedIndex by jtBtoMainScreenViewModel.selectedIndex.collectAsState()
    val sharedPreferenceUseCase = SharedPreferenceUseCase(Settings())
    val fromBookingsHandled by jtBtoMainScreenViewModel.fromBookingsHandled.collectAsState() // Add this line


    val providerInitialTab by jtBtoMainScreenViewModel.providerInitialTabIndex.collectAsState()

    val explore = stringResource(Res.string.explore)
    val bookings = stringResource(Res.string.bookings)
    val messages = stringResource(Res.string.messages)
    val more = stringResource(Res.string.more)
    val reports = stringResource(Res.string.report)
    val services = stringResource(Res.string.services_caps)

    val usertype = jtBtoMainScreenViewModel.userType
    val isAdmin = jtBtoMainScreenViewModel.isAdmin


    LaunchedEffect(usertype) {
        if (usertype == TYPE_PROVIDER) {
            jtBtoMainScreenViewModel.getWorkerServices()
        }
    }

    val hasWorkerServices by jtBtoMainScreenViewModel.hasWorkerServices.collectAsState()

    val disabledIndexes = remember(usertype, hasWorkerServices) {
        if (usertype == TYPE_PROVIDER && !hasWorkerServices) {
            // Only 0 and 3 enabled
            setOf(1, 2, 4)
        } else emptySet()
    }

    val navItemsList = remember {
        when {
            sharedPreferenceUseCase.getUser() == TYPE_CUSTOMER && isAdmin -> {
                listOf(
                    NavItem(
                        selectedIcon = Res.drawable.explore_active_ico,
                        unSelectedIcon = Res.drawable.explore_ico,
                        title = explore
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.bookings_active_ico,
                        unSelectedIcon = Res.drawable.bookings_ico,
                        title = bookings
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.messages_active_ico,
                        unSelectedIcon = Res.drawable.messages_ico,
                        title = messages
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.reports_active_ico,
                        unSelectedIcon = Res.drawable.reports_ico,
                        title = reports
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.more_active_ico,
                        unSelectedIcon = Res.drawable.more_ico,
                        title = more
                    )
                )
            }

            sharedPreferenceUseCase.getUser() == TYPE_CUSTOMER && !isAdmin -> {
                listOf(
                    NavItem(
                        selectedIcon = Res.drawable.explore_active_ico,
                        unSelectedIcon = Res.drawable.explore_ico,
                        title = explore
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.bookings_active_ico,
                        unSelectedIcon = Res.drawable.bookings_ico,
                        title = bookings
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.messages_active_ico,
                        unSelectedIcon = Res.drawable.messages_ico,
                        title = messages
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.more_active_ico,
                        unSelectedIcon = Res.drawable.more_ico,
                        title = more
                    )
                )
            }

            else -> {
                listOf(
                    NavItem(
                        selectedIcon = Res.drawable.explore_active_ico,
                        unSelectedIcon = Res.drawable.explore_ico,
                        title = explore
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.bookings_active_ico,
                        unSelectedIcon = Res.drawable.bookings_ico,
                        title = bookings
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.messages_active_ico,
                        unSelectedIcon = Res.drawable.messages_ico,
                        title = messages
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.services_active_ico,
                        unSelectedIcon = Res.drawable.services,
                        title = services
                    ),
                    NavItem(
                        selectedIcon = Res.drawable.more_active_ico,
                        unSelectedIcon = Res.drawable.more_ico,
                        title = more
                    )
                )
            }
        }
    }

    if (fromBookings && !fromBookingsHandled) {
        LaunchedEffect(fromBookings) {
            jtBtoMainScreenViewModel.handleFromBookingsNavigation()
        }
    }

    val fromServicesHandled by jtBtoMainScreenViewModel.fromServicesHandled.collectAsState()

    if (fromServices != 0 && fromServices == 1 && !fromServicesHandled) {
        LaunchedEffect(fromServices) {
            jtBtoMainScreenViewModel.handleFromServicesNavigation()
        }
    }

    val fromMessageHandled by jtBtoMainScreenViewModel.fromMessageHandled.collectAsState()
    if (fromMessage && !fromMessageHandled) {
        LaunchedEffect(fromMessage) {
            jtBtoMainScreenViewModel.handleFromMessageNavigation()
        }
    }


    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter)
            {
                 JTBottomNavigationBar(
                        navItem = navItemsList,
                        selectedIndex = selectedIndex,
                        onNavItemClicked = { index ->
                            jtBtoMainScreenViewModel.onNavItemClicked(index)
                        },
                     disabledIndexes = disabledIndexes
                 )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {

                when (selectedIndex) {

                    0 -> if (usertype == "user") {
                        JtExploreTabScreen(
                            navigateToAddressScreen = navigateAddress,
                            navigateToNotificationListScreen =navigateToNotificationListScreen,
                            navigateToCategoryScreen = navigateToCustomerCategoryScreen,
                            navigateToRentalsScreen = navigateToRentalsScreen,
                            navigateToAvailableServices = navigateToAvailableServicesScreen
                        )
                    } else if (usertype == "worker") {
                        ProviderDashBoardScreen(
                            navigateToNotificationListScreen = navigateToNotificationListScreen,
                            navigateToWebView = { webUrl,title->
                                navigateToWebScreen(webUrl,title)
                            }
                        )
                    }

                    1 -> if (usertype == TYPE_CUSTOMER) {
                        JtBookingsTabScreen(
                            navigateToOneOnOneChat = navigateToOneOnOneChat,
                            navigateToBookingInfoScreen = navigateToBookingInfoScreen
                        )
                    } else {
                        JtProviderBookingsTabScreen(
                            initialTabIndex = providerInitialTab,
                            navigateToOneOnOneChat = navigateToOneOnOneChat,
                            navigateToProviderBookingInfoScreen = navigateToProviderBookingInfoScreen,
                            navigateProviderCancelReasonScreen = navigateProviderCancelReasonScreen,
                            navigateToSendEstimationScreen = navigateToSendEstimationScreen,
                            navigateToInspectionReportScreen = navigateToInspectionReportScreen
                        )
                    }

                    2 -> JTChatListScreen(
                        navigateToOneOnOneChat = navigateToOneOnOneChat,
                        navigateToArchiveChat = navigateToArchiveChat
                    )

                    3 -> {
                        when {
                            usertype == TYPE_CUSTOMER && isAdmin -> {
                                JTAdminReportsScreen(
                                    navigateToWorkersScreen = navigateToWorkersScreen,
                                    navigateToUserScreen = navigateToUserScreen,
                                    navigateToServiceRequestScreen = navigateToServiceRequestScreen,
                                    navigateToManageServiceScreen = navigateToManageServiceScreen
                                )
                            }
                            usertype == TYPE_CUSTOMER && !isAdmin -> {
                                JTMoreTabScreen(
//                                    navigateToNotificationScreen = navigateToNotificationScreen,
                                    navigateToProfileEditScreen = navigateToProfileEditScreen,
                                    navigateToMediationGuidelines = navigateToMediationGuidelines,
                                    navigateToPrivacyAndPolicyScreen = navigateToPrivacyAndPolicyScreen,
                                    navigateToTermsAndConditionsScreen = navigateToTermsAndConditionsScreen,
                                    navigateToAboutBtoScreen = navigateToAboutBtoScreen,
                                    navigateToFaqScreen = navigateToFaqScreen,
                                    navigateToReportAnIssueScreen = navigateToReportAnIssueScreen,
                                    navigateToInviteFriendsScreen = navigateToInviteFriendsScreen,
                                    navigateAddress = navigateAddress,
                                    navigateToUserTypeScreen = navigateToUserTypeScreen,
                                    navigateToDeleteScreen = navigateToDeleteScreen,
                                    navigateToWalletScreen = navigateToWalletScreen,
                                    navigateToTransactionHistoryScreen = navigateToTransactionHistoryScreen,
                                    navigateToReviewAndRatingsScreen = navigateToReviewAndRatingsScreen,
                                    navigateToChatSupportScreen = navigateToChatSupportScreen,
                                    navigateToMainScreen = navigateToMainScreen,
                                    navigateToDocumentsScreen = navigateToDocumentsScreen,
                                    navigateToBankDetailsScreen = navigateToBankDetailsScreen,
                                    navigateToSignUpScreen = navigateSignUpScreen,
                                    navigateToProfileEditScreenFromDialog = navigateToProfileEditScreenFromDialog,
//                                    navController = navController
                                )
                            }
                            else -> {
                                ServicesTabScreen(
                                    navigateToCategoryScreen = navigateToCategoryScreen,
                                    navigateToAddServiceScreen = navigatetoAddServiceScreen,
                                    navigateToSelectLocation = navigateToSelectLocation,
                                    navigateToWebView =  navigateToWebScreen
                                )
                            }
                        }
                    }

                    4 -> {
                        when {
                            usertype == TYPE_CUSTOMER && isAdmin -> {
                                JTMoreTabScreen(
//                                    navigateToNotificationScreen = navigateToNotificationScreen,
                                    navigateToProfileEditScreen = navigateToProfileEditScreen,
                                    navigateToMediationGuidelines = navigateToMediationGuidelines,
                                    navigateToPrivacyAndPolicyScreen = navigateToPrivacyAndPolicyScreen,
                                    navigateToTermsAndConditionsScreen = navigateToTermsAndConditionsScreen,
                                    navigateToAboutBtoScreen = navigateToAboutBtoScreen,
                                    navigateToFaqScreen = navigateToFaqScreen,
                                    navigateToReportAnIssueScreen = navigateToReportAnIssueScreen,
                                    navigateToInviteFriendsScreen = navigateToInviteFriendsScreen,
                                    navigateAddress = navigateAddress,
                                    navigateToUserTypeScreen = navigateToUserTypeScreen,
                                    navigateToDeleteScreen = navigateToDeleteScreen,
                                    navigateToWalletScreen = navigateToWalletScreen,
                                    navigateToTransactionHistoryScreen = navigateToTransactionHistoryScreen,
                                    navigateToReviewAndRatingsScreen = navigateToReviewAndRatingsScreen,
                                    navigateToChatSupportScreen = navigateToChatSupportScreen,
                                    navigateToMainScreen = navigateToMainScreen,
                                    navigateToDocumentsScreen = navigateToDocumentsScreen,
                                    navigateToBankDetailsScreen = navigateToBankDetailsScreen,
                                    navigateToSignUpScreen = navigateSignUpScreen,
                                    navigateToProfileEditScreenFromDialog= navigateToProfileEditScreenFromDialog,
//                                    navController = navController



                                )
                            }
                            usertype == TYPE_PROVIDER -> {
                                JTMoreTabScreen(
//                                    navigateToNotificationScreen = navigateToNotificationScreen,
                                    navigateToProfileEditScreen = navigateToProfileEditScreen,
                                    navigateToMediationGuidelines = navigateToMediationGuidelines,
                                    navigateToPrivacyAndPolicyScreen = navigateToPrivacyAndPolicyScreen,
                                    navigateToTermsAndConditionsScreen = navigateToTermsAndConditionsScreen,
                                    navigateToAboutBtoScreen = navigateToAboutBtoScreen,
                                    navigateToFaqScreen = navigateToFaqScreen,
                                    navigateToReportAnIssueScreen = navigateToReportAnIssueScreen,
                                    navigateToInviteFriendsScreen = navigateToInviteFriendsScreen,
                                    navigateAddress = navigateAddress,
                                    navigateToUserTypeScreen = navigateToUserTypeScreen,
                                    navigateToDeleteScreen = navigateToDeleteScreen,
                                    navigateToWalletScreen = navigateToWalletScreen,
                                    navigateToTransactionHistoryScreen = navigateToTransactionHistoryScreen,
                                    navigateToReviewAndRatingsScreen = navigateToReviewAndRatingsScreen,
                                    navigateToChatSupportScreen = navigateToChatSupportScreen,
                                    navigateToMainScreen = navigateToMainScreen,
                                    navigateToDocumentsScreen = navigateToDocumentsScreen,
                                    navigateToBankDetailsScreen = navigateToBankDetailsScreen,
                                    navigateToSignUpScreen = navigateSignUpScreen,
                                    navigateToProfileEditScreenFromDialog = navigateToProfileEditScreenFromDialog,
//                                    navController = navController


                                )
                            }
                            else -> {
                                JtExploreTabScreen(
                                    navigateToAddressScreen = navigateAddress,
                                    navigateToNotificationListScreen= navigateToNotificationListScreen,
                                    navigateToCategoryScreen = navigateToCustomerCategoryScreen,
                                    navigateToRentalsScreen = navigateToRentalsScreen,
                                    navigateToAvailableServices = navigateToAvailableServicesScreen
                                )
                            }
                        }
                    }

                    else -> JtExploreTabScreen(
                        navigateToAddressScreen = navigateAddress,
                        navigateToNotificationListScreen = navigateToNotificationListScreen,
                        navigateToCategoryScreen = navigateToCustomerCategoryScreen,
                        navigateToRentalsScreen = navigateToRentalsScreen,
                        navigateToAvailableServices = navigateToAvailableServicesScreen
                    )
                }
            }
        }
    )
}