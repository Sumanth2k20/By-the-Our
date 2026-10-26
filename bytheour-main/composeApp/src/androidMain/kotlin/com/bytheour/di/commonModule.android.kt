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
import com.bytheour.utils.AndroidChatSocket
import com.bytheour.utils.ChatSocket
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module


actual val commonModule: Module = module {
        //chat
        single<ChatSocket> { AndroidChatSocket(CHAT_BASE_URL) }

        viewModelOf(::JTLocationFetchingViewModel)
        viewModelOf(::JTLoginViewModel)
        viewModelOf(::OtpViewModel)
        viewModelOf(::JTSignUpViewModel)
        viewModelOf(::JTWelcomeViewModel)
        viewModelOf(::JTChatListViewModel)
        viewModelOf(::JTAddressListViewModel)
        viewModelOf(::JTMoreTabScreenViewModel)
        viewModelOf(::JTProfileEditScreenViewModel)
        viewModelOf(::JTBtoMainScreenViewModel)
        viewModelOf(::JTUserTypeViewModel)
        viewModelOf(::PrivacyAndPolicyViewModel)
        viewModelOf(::JTMediationGuidelinesViewModel)
        viewModelOf(::JTSelectLocationViewModel)
        viewModelOf(::JTGoogleViewmodel)
        viewModelOf(::JTReportAnIssueScreenViewModel)
        viewModelOf(::FaqViewModel)
        viewModelOf(::DeleteScreenViewModel)
        viewModelOf(::AdminUsersViewModel)
        viewModelOf(::UserEditScreenViewModel)
        viewModelOf(::JTSplashViewmodel)
        viewModelOf(::OneOnOneChatScreenViewModel)
        viewModelOf(::ExploreTabViewModel)
        viewModelOf(::TrendingServicesViewModel)
        viewModelOf(::JTBookingTabViewmodel)
        viewModelOf(::JTProviderBookingTabScreenViewmodel)
        viewModelOf(::CancelProviderBookingViewModel)
        viewModelOf(::CancelBookingViewModel)
        viewModelOf(::AddCardDetailsViewModel)
        viewModelOf(::EstimateViewModel)
        viewModelOf(::MediationRefundViewModelProvider)
        viewModelOf(::SelectionDateOfServiceViewModel)
        viewModelOf(::ViewProviderProfileViewModel)
        viewModelOf(::CategoriesViewModel)
        viewModelOf(::JTNotificationScreenViewModel)
        viewModelOf(::TransactionHistoryScreenViewModel)
        viewModelOf(::TransactionDetailScreenViewModel)
        viewModelOf(::ReviewsAndDetailScreenViewmodel)
        viewModelOf(::ChatSupportScreenViewModel)
        viewModelOf(::ChatSupportViewModel)
        viewModelOf(::NotificationListScreenViewModel)
        viewModelOf(::ImageUploadViewModel)
        viewModelOf(::DocumentationScreenViewModel)
        viewModelOf(::SelectPlanViewModel)
        viewModelOf(::BankDetailsViewModel)
        viewModelOf(::InspectionReportScreenViewModel)
        viewModelOf(::InspectionDeclineViewModel)



        //Admin
        viewModelOf(::JtAdminReportsScreenViewModel)
        viewModelOf(::JtAdminReportsScreenViewModel)
        viewModelOf(::AdminManageServiceScreenViewModel)
        viewModelOf(::AdminServiceEditedScreenViewmodel)
        viewModelOf(::WorkersScreenViewModel)
        viewModelOf(::EditWorkerDetailsScreenViewModel)
        viewModelOf(::JtDownLoadsScreenViewModel)
        viewModelOf(::ServiceScreenViewModel)
        viewModelOf(::JTServiceRequestListViewModel)
        viewModelOf(::ManageServiceEditScreenViewModel)
        viewModelOf(::EntryViewModel)
        viewModelOf(::ProviderHomeViewModel)
        viewModelOf(::ServicesListViewModel)
        viewModelOf(::ServiceTabScreenViewModel)
        viewModelOf(::ProviderSelectServiceScreenViewModel)
        viewModelOf(::AddServiceScreenViewModel)
        viewModelOf(::ServiceRequestScreenViewModel)



}
