//
//  SearchAnimationView.swift
//  iosApp
//
//  Created by VenkataKarthik on 25/06/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

class IOSSearchAnimationViewFactory: ISearchAnimation {
    static var shared = IOSSearchAnimationViewFactory()
    func createSearchAnimationView() -> UIViewController {
        let view = SearchAnimationView()
        
        let hostingController = UIHostingController(rootView: view)
        hostingController.view.backgroundColor = .clear
        hostingController.view.isOpaque = false

        return hostingController
    }
    
}
struct SearchAnimationView: View {
    @State private var isAnimating = true

    var body: some View {
        ZStack{
            
            VStack {
                ZStack {
                    LottieView(animationName: "searching_provider", loopMode: .loop, isPlaying: isAnimating)
                        .frame(width: 90, height: 90)
                }
            }
        }
    }
}


#Preview {
    SearchAnimationView()
}
