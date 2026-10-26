import UIKit
import SwiftUI
import ComposeApp
import StripeCore

struct ComposeView: UIViewControllerRepresentable {
    @State private var fcmToken: String = ""
    
    func makeUIViewController(context: Context) -> UIViewController {
        // Register callback for when FCM token is available
            AppDelegate.fcmTokenCallback = { token in
                DispatchQueue.main.async {
                    self.fcmToken = token
                    print("✅ FCM Token received in Swift: \(token)")
                    
                    MainViewControllerKt.fcmTokenCallback?(token)
                }
            }
     
        
        let vc = MainViewControllerKt.MainViewController(
            firebaseAuthService: FirebaseAuthServiceImpl(),
            nativeLocationAnimation: IOSNativeLocationViewFactory.shared,
            nativeSearchAnimation: IOSSearchAnimationViewFactory.shared,
            onFcmToken: { token in
                print("📲 Token received in Swift → Kotlin bridge: \(token)")
            }
        )
        
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}


struct ContentView: View {
  
    var body: some View {
        ComposeView()
            .ignoresSafeArea(edges: .all)
            .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}

