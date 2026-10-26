//
//  AnimationView.swift
//  iosApp
//
//  Created by MadhushaKondabathini on 07/05/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

class IOSNativeLocationViewFactory: ILocationAnimation {
    static var shared = IOSNativeLocationViewFactory()
    func createLocationAnimationView() -> UIViewController {
        let view = AnimationView()
        
        let hostingController = UIHostingController(rootView: view)
        hostingController.view.backgroundColor = .clear
        hostingController.view.isOpaque = false

        return hostingController
    }
    
}
struct AnimationView: View {
    @State private var isAnimating = true
    @State private var isLoactionFetched: Bool = true

    var body: some View {
        ZStack{
        
            Image("ic_map_bg")
                .resizable()
                .ignoresSafeArea()
            
            VStack {
                ZStack {
                    LottieView(animationName: "location", loopMode: .loop, isPlaying: isAnimating)
                        .frame(width: 400, height: 400)
                    
                    
                    
                    Image("ic_LocationPin") // Use your image name here
                        .resizable()
                        .frame(width: 24, height: 42)
                        .foregroundColor(.yellow)
                }
                        
            }
        }
    }
}
 


#Preview {
    AnimationView()
}
