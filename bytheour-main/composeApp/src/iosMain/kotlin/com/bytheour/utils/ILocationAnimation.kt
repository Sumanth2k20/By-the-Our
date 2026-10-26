package com.bytheour.utils

import platform.UIKit.UIViewController

interface ILocationAnimation {
    fun createLocationAnimationView(): UIViewController
}

interface ISearchAnimation {
    fun createSearchAnimationView(): UIViewController
}