package com.bytheour.di

import com.bytheour.core.JTURLS.CHAT_BASE_URL
import com.bytheour.presentations.screens.JTBtoMainScreenViewModel
import com.bytheour.presentations.screens.JTUserTypeViewModel
import com.bytheour.presentations.screens.location.JTLocationFetchingViewModel
import com.bytheour.presentations.screens.login.EntryViewModel
import com.bytheour.presentations.screens.login.JTLoginViewModel
import com.bytheour.presentations.screens.otp.OtpViewModel
import com.bytheour.presentations.screens.services.TrendingServicesViewModel
import com.bytheour.presentations.screens.signup.JTSignUpViewModel
import com.bytheour.presentations.screens.splash.JTSplashViewmodel
import com.bytheour.presentations.screens.tabs.bookings.AddCardDetailsViewModel
import com.bytheour.presentations.screens.tabs.bookings.CancelBookingViewModel
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.CancelProviderBookingViewModel
import com.bytheour.presentations.screens.tabs.bookings.ChatSupportViewModel
import com.bytheour.presentations.screens.tabs.bookings.JTBookingTabViewmodel
import com.bytheour.presentations.screens.tabs.bookings.ServicesListViewModel
import com.bytheour.presentations.screens.tabs.bookings.ViewProviderProfileViewModel
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionDeclineViewModel
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionReportScreenViewModel
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.EstimateViewModel
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.JTProviderBookingTabScreenViewmodel
import com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow.MediationRefundViewModelProvider
import com.bytheour.presentations.screens.tabs.explore.ExploreTabViewModel
import com.bytheour.presentations.screens.tabs.explore.notification.NotificationListScreenViewModel
import com.bytheour.presentations.screens.tabs.explore.searchingAndRequestSentScreens.SelectionDateOfServiceViewModel
import com.bytheour.presentations.screens.tabs.messages.JTChatListViewModel
import com.bytheour.presentations.screens.tabs.messages.OneOnOneChatScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.delete.DeleteScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.FaqViewModel
import com.bytheour.presentations.screens.tabs.moreTab.JTMoreTabScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.PrivacyAndPolicyViewModel
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTAddressListViewModel
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTGoogleViewmodel
import com.bytheour.presentations.screens.tabs.moreTab.adress.JTSelectLocationViewModel
import com.bytheour.presentations.screens.tabs.moreTab.bankDetails.BankDetailsViewModel
import com.bytheour.presentations.screens.tabs.moreTab.chatSupport.ChatSupportScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.documentation.DocumentationScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.mediationGuideline.JTMediationGuidelinesViewModel
import com.bytheour.presentations.screens.tabs.moreTab.notification.JTNotificationScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.profile.JTProfileEditScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.reportAnIssue.JTReportAnIssueScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.reviewAndRatings.ReviewsAndDetailScreenViewmodel
import com.bytheour.presentations.screens.tabs.moreTab.transactionHistory.TransactionDetailScreenViewModel
import com.bytheour.presentations.screens.tabs.moreTab.transactionHistory.TransactionHistoryScreenViewModel
import com.bytheour.presentations.screens.tabs.providerHomeTab.ProviderHomeViewModel
import com.bytheour.presentations.screens.tabs.reports.AdminServiceEditedScreenViewmodel
import com.bytheour.presentations.screens.tabs.reports.AdminUsersViewModel
import com.bytheour.presentations.screens.tabs.reports.JTServiceRequestListViewModel
import com.bytheour.presentations.screens.tabs.reports.JtAdminReportsScreenViewModel
import com.bytheour.presentations.screens.tabs.reports.JtDownLoadsScreenViewModel
import com.bytheour.presentations.screens.tabs.reports.ServiceScreenViewModel
import com.bytheour.presentations.screens.tabs.reports.UserEditScreenViewModel
import com.bytheour.presentations.screens.tabs.reports.services.AdminManageServiceScreenViewModel
import com.bytheour.presentations.screens.tabs.reports.services.ManageServiceEditScreenViewModel
import com.bytheour.presentations.screens.tabs.reports.workers.EditWorkerDetailsScreenViewModel
import com.bytheour.presentations.screens.tabs.reports.workers.WorkersScreenViewModel
import com.bytheour.presentations.screens.tabs.servicesTab.AddServiceScreenViewModel
import com.bytheour.presentations.screens.tabs.servicesTab.CategoriesViewModel
import com.bytheour.presentations.screens.tabs.servicesTab.ImageUploadViewModel
import com.bytheour.presentations.screens.tabs.servicesTab.ProviderSelectServiceScreenViewModel
import com.bytheour.presentations.screens.tabs.servicesTab.SelectPlanViewModel
import com.bytheour.presentations.screens.tabs.servicesTab.ServiceRequestScreenViewModel
import com.bytheour.presentations.screens.tabs.servicesTab.ServiceTabScreenViewModel
import com.bytheour.presentations.screens.wellComeScreens.JTWelcomeViewModel
import com.bytheour.utils.FirebaseAuthService
import com.bytheour.utils.FirebaseAuthServiceImpl
import org.koin.core.module.Module
import com.bytheour.utils.ChatSocket
import com.bytheour.utils.IosChatSocketViaBridge
import org.koin.dsl.module

actual val commonModule: Module = module {

    //Chat
    single<ChatSocket> { IosChatSocketViaBridge(CHAT_BASE_URL) }


    single<FirebaseAuthService> { FirebaseAuthServiceImpl() }
    factory{JTLocationFetchingViewModel(get())}
    factory{JTLoginViewModel(get(),get())}
    factory{OtpViewModel(get(),get())}
    factory{JTSignUpViewModel(get(),get())}
    factory{JTWelcomeViewModel()}
    factory{JTChatListViewModel(get(),get())}
    factory{JTAddressListViewModel(get(),get(),get())}
    factory{JTMoreTabScreenViewModel(get(),get(),get(),get())}
    factory{JTProfileEditScreenViewModel(get(),get())}
    factory{JTBtoMainScreenViewModel(get(),get())}
    factory{JTUserTypeViewModel(get())}
    factory { PrivacyAndPolicyViewModel(get()) }
    factory { JTSplashViewmodel(get(), get()) }
    factory{JTMediationGuidelinesViewModel(get())}
    factory{JTReportAnIssueScreenViewModel(get())}
    factory { JTSelectLocationViewModel(get(),get()) }
    factory { JTGoogleViewmodel() }
    factory { FaqViewModel(get()) }
    factory { DeleteScreenViewModel(get(),get()) }
    factory { OneOnOneChatScreenViewModel(get(),get(),get(),get(),get()) }
    factory { JTBookingTabViewmodel(get(),get(),get(),get()) }
    factory{ ExploreTabViewModel(get(),get(),get()) }
    factory { TrendingServicesViewModel(get(),get()) }
    factory { JTProviderBookingTabScreenViewmodel(get(), get()) }
    factory { EntryViewModel(get(),get()) }
    factory { AddCardDetailsViewModel(get()) }
    factory { CancelProviderBookingViewModel(get(),get()) }
    factory { EstimateViewModel(get()) }
    factory { CancelBookingViewModel(get(),get()) }
    factory { MediationRefundViewModelProvider(get()) }
    factory{SelectionDateOfServiceViewModel(get(),get())}
    factory { ViewProviderProfileViewModel(get()) }
    factory { JTNotificationScreenViewModel() }
    factory { CategoriesViewModel(get(),get(),get()) }
    factory { TransactionHistoryScreenViewModel(get()) }
    factory { TransactionDetailScreenViewModel(get()) }
    factory { ReviewsAndDetailScreenViewmodel(get()) }
    factory { ChatSupportScreenViewModel(get(),get()) }
    factory { NotificationListScreenViewModel(get()) }
    factory { ChatSupportViewModel(get()) }
    factory { ImageUploadViewModel(get(),get(),get()) }
    factory { DocumentationScreenViewModel(get()) }
    factory { SelectPlanViewModel(get(),get(),get()) }
    factory { BankDetailsViewModel(get()) }
    factory { InspectionReportScreenViewModel(get()) }
    factory { InspectionDeclineViewModel(get()) }
    //Admin
    factory{ JtAdminReportsScreenViewModel(get())}
    factory{ JtDownLoadsScreenViewModel(get())}
    factory{ AdminServiceEditedScreenViewmodel(get(),get())}
    factory { AdminUsersViewModel(get()) }
    factory { UserEditScreenViewModel(get(),get()) }
    factory{ AdminManageServiceScreenViewModel(get())}
    factory{ WorkersScreenViewModel(get())}
    factory{ EditWorkerDetailsScreenViewModel(get(),get())}
    factory { ServiceScreenViewModel(get()) }
    factory { JTServiceRequestListViewModel(get()) }
    factory { ManageServiceEditScreenViewModel(get(),get()) }
    factory { ServicesListViewModel() }
    factory { ProviderHomeViewModel(get(),get(),get(),get()) }
    factory { ServiceTabScreenViewModel(get(),get(),get(),get()) }
    factory { ProviderSelectServiceScreenViewModel(get()) }
    factory { AddServiceScreenViewModel(get(),get(),get(),get()) }
    factory { ServiceRequestScreenViewModel(get()) }

}
