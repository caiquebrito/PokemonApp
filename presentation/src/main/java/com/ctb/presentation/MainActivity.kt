package com.ctb.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ctb.design.compose.component.QuickStartNavHost
import com.ctb.design.compose.theme.QuickStartTheme
import com.ctb.presentation.urlshortener.UrlShortenerRoute
import com.ctb.presentation.urlshortener.urlShortener
import com.ctb.presentation.urlshortenerdetail.openUrlShortenerDetail
import com.ctb.presentation.urlshortenerdetail.urlShortenerDetail

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickStartTheme {
                val navController = rememberNavController()

                QuickStartNavHost(
                    navController = navController,
                    startDestination = UrlShortenerRoute,
                ) {
                    urlShortener(
                        closeFlow = { navController.popBackStack() },
                        onItemClicked = { item ->
                            navController.openUrlShortenerDetail(item)
                        },
                    )
                    urlShortenerDetail(
                        onBackClick = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
