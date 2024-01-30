package com.ppam.basicmusicapp

import androidx.annotation.DrawableRes

sealed class Screen (val title: String, val route: String) {


    sealed class BottomScreen(
        var bTitle: String, val bRoute: String, @DrawableRes val icon: Int
    ): Screen(bTitle,bRoute) {
        object Home: BottomScreen("Home", "home", R.drawable.baseline_library_music_24)

        object Library: BottomScreen("library", "library", R.drawable.baseline_smart_display_24)

        object Browser: BottomScreen("Browse", "browse", R.drawable.baseline_open_in_browser_24)

    }

    sealed class DrawerScreen(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int)
        : Screen(dTitle, dRoute) {
            object Account: DrawerScreen (
                "Account",
                "account",
                R.drawable.baseline_account_box_24
            )

        object Subscription: DrawerScreen (
            "Subscription",
            "subscribe",
            R.drawable.baseline_subscriptions_24
        )

        object AddAccount: DrawerScreen (
            "Add Account",
            "add_account",
            R.drawable.baseline_person_add_alt_1_24
        )
        }
}


val ScreenInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Library,
    Screen.BottomScreen.Browser
)
val screenInDrawer = listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.Subscription,
    Screen.DrawerScreen.AddAccount
)