import SwiftUI
import Firebase
import Stripe

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self)
      private var appDelegate

//    let persistenceController = PersistenceController.shared
    var body: some Scene {
       
        WindowGroup {
            
            ContentView()
//            AnimationView()
//                .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }
}
