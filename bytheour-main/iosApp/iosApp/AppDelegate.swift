//
//  AppDelegate.swift
//  iosApp
//
//  Created by Sai Sumanth on 26/06/25.
//

import UIKit
import Firebase
import ComposeApp           // ← if you really need it here
import UserNotifications
import FirebaseMessaging
import GoogleMaps
import Stripe
/// Plain class – **no `@UIApplicationMain`**
class AppDelegate: NSObject,
                   UIApplicationDelegate,
                   UNUserNotificationCenterDelegate,
                   MessagingDelegate {

    static var fcmTokenCallback: ((String) -> Void)?


    // MARK: - Launch

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        // Firebase
        FirebaseApp.configure()
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
        Messaging.messaging().delegate = self
        IosOtpBridgeRegistry.shared.register(impl: IosOtpBridgeImpl())
        GMSServices.provideAPIKey("AIzaSyDBumNKkJ2oZTycPAsQRRFy8c-31PKUuHs")
        IosGoogleMapBridgeRegistry.shared.register(impl: IOSGoogleMapBridgeImpl())
        StripeUiBridgeRegistry().register(impl: StripeUiBridgeImpl())
        StripeAPI.defaultPublishableKey = ConstantsKt.PUBLISHABLE_KEY

        IosChatBridgeRegistry.shared.register(impl: IOSSocketBridgeImpl())
        // NotificationCenter
        let center = UNUserNotificationCenter.current()
        center.delegate = self
        center.requestAuthorization(options: [.alert, .sound, .badge]) { granted, _ in
            print("🔔 permission:", granted)
            DispatchQueue.main.async {
                application.registerForRemoteNotifications()
            }
        }
        if launchOptions?[.remoteNotification] != nil {
            NotificationHandler.shared.wasLaunchedFromNotification = true
        }
        return true
    }

    // MARK: - APNs Token
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("🔥 FCM token: \(fcmToken ?? "nil")")
        if let token = fcmToken {
            Self.fcmTokenCallback?(token)
        }
    }

    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data) {
        let token = deviceToken.map { String(format: "%02.2hhx", $0) }.joined()
        print("📱 APNs token:", token)
        Messaging.messaging().apnsToken = deviceToken
    }


    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("❌ APNs registration failed:", error.localizedDescription)
    }

    // MARK: - Handle Background Messages
    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable : Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {

        print("📬 Background FCM:", userInfo)
        Messaging.messaging().appDidReceiveMessage(userInfo)
        NotificationHandler.shared.wasLaunchedFromNotification = true
        completionHandler(.newData)
    }

    // MARK: - Foreground Notification
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        print("🔔 Foreground FCM:", notification.request.content.userInfo)
        completionHandler([.alert, .sound, .badge])
    }
    
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo
        print("Notification received: ================================  \(userInfo)")
        Messaging.messaging().appDidReceiveMessage(userInfo)
        NotificationHandler.shared.wasLaunchedFromNotification = true
        if let type = userInfo["type"] as? String {
            NotificationHandler.shared.notificationType = type
        }
        completionHandler()
    }

    
    
    func showAlert(message: String) {
        let alert = UIAlertController(title: "Alert", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Click", style: .default, handler: nil))
        DispatchQueue.main.async {
            if let topVC = self.topMostViewController() {
                topVC.present(alert, animated: true, completion: nil)
            } else {
                print("No top view controller found")
            }
        }
    }
    
    func topMostViewController() -> UIViewController? {
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let rootVC = windowScene.windows.first(where: { $0.isKeyWindow })?.rootViewController else {
            return nil
        }
        var topController = rootVC
        while let presentedViewController = topController.presentedViewController {
            topController = presentedViewController
        }
        return topController
    }
}
